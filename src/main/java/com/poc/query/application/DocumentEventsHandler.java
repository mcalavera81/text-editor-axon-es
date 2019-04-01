package com.poc.query.application;

import com.poc.command.event.AppendedLineEvent;
import com.poc.command.event.DocumentCreatedEvent;
import com.poc.command.event.DocumentDeletedEvent;
import com.poc.command.event.UpdatedLineEvent;
import com.poc.query.domain.repository.DocumentViewRepository;
import com.poc.query.domain.repository.dto.DocumentViewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@XSlf4j
@RequiredArgsConstructor
public class DocumentEventsHandler {


    private final DocumentViewRepository repository;

    @EventHandler
    public void on(DocumentCreatedEvent evt) {
        log.trace("projecting {}", evt);
        repository.save(new DocumentViewDto(evt.getId(), evt.getName()));
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
            doc.appendLine(evt.getLine());
            repository.save(doc);
        });
    }


    @EventHandler
    public void on(UpdatedLineEvent evt) {
        log.trace("projecting {}", evt);
        repository.findById(evt.getId()).ifPresent(doc -> {

        });

    }

    @QueryHandler
    public DocumentViewDto handle(DocumentQuery query) {
        return repository.findById(query.getDocId()).orElse(null);
    }
}
