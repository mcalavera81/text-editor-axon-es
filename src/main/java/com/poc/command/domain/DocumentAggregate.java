package com.poc.command.domain;

import com.poc.command.event.AppendedLineEvent;
import com.poc.command.event.DocumentCreatedEvent;
import com.poc.command.event.DocumentDeletedEvent;
import com.poc.command.event.UpdatedLineEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class DocumentAggregate {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @AggregateIdentifier
    private String id;

    private boolean deleted;

    private int numberOfLines;
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
    public void handle(DeleteDocumentCommand cmd) {
        apply(new DocumentDeletedEvent(cmd.getId()));
    }

    @CommandHandler
    public void handle(AppendLineCommand cmd) {
        apply(new AppendedLineEvent(cmd.getId(), cmd.getLine()));
    }


    @CommandHandler
    public void handle(UpdateLineCommand cmd) {
        if(cmd.getNumber() <= 0) throw new IllegalArgumentException("amount <= 0");
        if(cmd.getNumber() > numberOfLines){
            throw new IllegalStateException("number > total number of lines");
        }
        apply(new UpdatedLineEvent(cmd.getId(), cmd.getNumber(),cmd.getLine()));
    }


    @EventSourcingHandler
    protected void on(DocumentCreatedEvent evt) {
        log.debug("applying {}", evt);
        this.id = evt.getId();
        this.deleted = false;
        this.numberOfLines = 0;
    }

    @EventSourcingHandler
    protected void on(DocumentDeletedEvent evt) {
        log.debug("applying {}", evt);
        this.deleted = true;
    }

    @EventSourcingHandler
    protected void on(AppendedLineEvent evt) {
        numberOfLines++;
        log.debug("applying {}", evt);
    }

    @EventSourcingHandler
    protected void on(UpdatedLineEvent evt) {log.debug("applying {}", evt);}
}