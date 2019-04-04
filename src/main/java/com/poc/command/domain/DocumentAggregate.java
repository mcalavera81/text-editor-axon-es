package com.poc.command.domain;

import com.poc.command.event.*;
import com.poc.query.domain.repository.dto.AggregateHistoryDTO;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class DocumentAggregate {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private EventStore eventStore;

    @AggregateIdentifier
    private String id;

    private boolean deleted;

    private List<String> lines;
    /*
    Required by Axon to build a default Aggregate prior to Event Sourcing
    Constructor required for reconstruction
     */
    public DocumentAggregate() {
        log.debug("empty constructor invoked");
    }

    //IllegalArgumentException
    //IllegalStateException

    @CommandHandler
    public DocumentAggregate(CreateDocumentCommand cmd) {
        log.debug("handling {}", cmd);
        apply(new DocumentCreatedEvent(cmd.getId(), cmd.getName()));
    }

    @CommandHandler
    public void delete(DeleteDocumentCommand cmd) {
        if(deleted) throw new IllegalStateException("Document deleted");
        apply(new DocumentDeletedEvent(cmd.getId()));
    }

    @CommandHandler
    public void append(AppendLineCommand cmd) {
        if(deleted) throw new IllegalStateException("Document deleted");

        lines.add(cmd.getLine());
        apply(new AppendedLineEvent(cmd.getId(), cmd.getLine()));
    }


    @CommandHandler
    public void updateLine(UpdateLineCommand cmd) {
        if(deleted) throw new IllegalStateException("Document deleted");
        if(cmd.getNumber() <= 0) throw new IllegalArgumentException("amount <= 0");
        if(cmd.getNumber() > lines.size()){
            throw new IllegalStateException("number > total number of lines");
        }

        String oldLine = lines.get(cmd.getNumber()-1);
        apply(new UpdatedLineEvent(cmd.getId(), cmd.getNumber(),cmd.getLine(), oldLine));
        lines.set(cmd.getNumber()-1,cmd.getLine());
    }

    @CommandHandler
    public void undo(UndoCommand cmd) {
        if(deleted) throw new IllegalStateException("Document deleted");

        List<AggregateHistoryDTO> evts = eventStore.readEvents(cmd.getId())
                .asStream()
                .map(this::domainEventToAggregateHistory)
                .collect(Collectors.toList());

        Collections.reverse(evts);
        int skip = 0;
        for (AggregateHistoryDTO evt : evts) {
            if(evt.getEvent().isCompensation()){
                skip++;
                continue;
            }
            if(skip>0) {
                skip--;
                continue;
            }
            if(evt.getEvent().isCompensatable()){
                UndoEvent undoEvent = evt.getEvent().buildCompensation();
                apply(undoEvent);
                break;
            }
        }
    }


    @EventSourcingHandler
    protected void on(DocumentCreatedEvent evt) {
        log.debug("applying {}", evt);
        this.id = evt.getId();
        this.deleted = false;
        this.lines= new ArrayList<>();
    }

    @EventSourcingHandler
    protected void on(DocumentDeletedEvent evt) {
        log.debug("applying {}", evt);
        this.deleted = true;
    }

    @EventSourcingHandler
    protected void on(AppendedLineEvent evt) {
        log.debug("applying {}", evt);
        lines.add(evt.getLine());
    }

    @EventSourcingHandler
    protected void on(UpdatedLineEvent evt) {
        log.debug("applying {}", evt);
        lines.set(evt.getNumber()-1,evt.getLine());
    }

    @EventSourcingHandler
    protected void on(UndoAppendedLineEvent evt) {
        log.debug("applying {}", evt);
        lines.remove(lines.size()-1);
    }

    @EventSourcingHandler
    protected void on(UndoUpdatedLineEvent evt) {
        log.debug("applying {}", evt);
        lines.set(evt.getNumber()-1,evt.getLine());
    }

    private AggregateHistoryDTO domainEventToAggregateHistory(DomainEventMessage<?> event)
    {
        return new AggregateHistoryDTO(event.getPayloadType().getSimpleName(), (DocumentEvent) event.getPayload(), event.getSequenceNumber(),
                event.getTimestamp());
    }
}