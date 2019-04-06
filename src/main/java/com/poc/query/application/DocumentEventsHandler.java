package com.poc.query.application;

import com.poc.command.event.*;
import com.poc.query.domain.repository.DocumentViewRepository;
import com.poc.query.domain.repository.dto.DocumentViewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@XSlf4j
@RequiredArgsConstructor
public class DocumentEventsHandler {


    private final DocumentViewRepository repository;

    @EventHandler
    public void on(DocumentCreatedEvent evt) {
        log.trace("projecting {}", evt);
        repository.save(new DocumentViewDto(evt.getId(), evt.getDocName()));
    }

    @EventHandler
    public void on(DocumentDeletedEvent evt) {
        log.trace("projecting {}", evt);
        repository.deleteById(evt.getId());
    }

    @EventHandler
    public void on(AppendedLineEvent evt) {
        log.trace("projecting {}", evt);
        repository.findById(evt.getId()).ifPresent(doc -> {
            doc.appendLine(evt.getAppendedText());
            repository.save(doc);
        });
    }


    @EventHandler
    public void on(UpdatedLineEvent evt) {
        log.trace("projecting {}", evt);
        repository.findById(evt.getId()).ifPresent(doc -> {
            doc.updateLine(evt.getLineNumber(),evt.getText());
            repository.save(doc);
        });

    }


    @EventHandler
    protected void on(InsertedLineEvent evt) {
        log.debug("projecting {}", evt);
        repository.findById(evt.getId()).ifPresent(doc -> {
            doc.insertLine(evt.getLineNumber(), evt.getInsertedText());
            repository.save(doc);
        });

    }

    @EventHandler
    protected void on(RemovedLineEvent evt) {
        log.debug("projecting {}", evt);
        repository.findById(evt.getId()).ifPresent(doc -> {
            doc.removeLine(evt.getLineNumber());
            repository.save(doc);
        });

    }

    @EventHandler
    protected void on(UndoAppendedLineEvent evt) {
        log.debug("applying {}", evt);
        repository.findById(evt.getId()).ifPresent(doc -> {
            doc.getLines().remove(doc.getLines().size()-1);
            repository.save(doc);
        });
    }

    @EventHandler
    protected void on(UndoUpdatedLineEvent evt) {
        log.debug("applying {}", evt);
        repository.findById(evt.getId()).ifPresent(doc -> {
            doc.getLines().set(evt.getLineNumber()-1,evt.getText());
            repository.save(doc);
        });
    }

    @EventHandler
    protected void on(UndoInsertedLineEvent evt) {
        log.debug("projecting {}", evt);
        repository.findById(evt.getId()).ifPresent(doc -> {
            doc.removeLine(evt.getLineNumber());
            repository.save(doc);
        });

    }

    @EventHandler
    protected void on(UndoRemovedLineEvent evt) {
        log.debug("projecting {}", evt);
        repository.findById(evt.getId()).ifPresent(doc -> {
            doc.insertLine(evt.getLineNumber(),evt.getText());
            repository.save(doc);
        });

    }

    @QueryHandler
    public DocumentViewDto findDoc(DocumentQuery query) {
        return repository.findById(query.getDocId()).orElse(null);
    }

}
