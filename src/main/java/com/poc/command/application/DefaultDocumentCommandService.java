package com.poc.command.application;

import com.poc.command.domain.*;
import com.poc.command.dto.CreateDocumentRequest;
import com.poc.command.dto.DocumentLineDto;
import com.poc.command.event.DocumentEvent;
import com.poc.query.domain.repository.dto.AggregateHistoryDTO;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultDocumentCommandService implements DocumentCommandService {

    private final CommandGateway commandGateway;
    private final EventStore eventStore;
    private final Repository<DocumentAggregate> repository;

    @Override
    public CompletableFuture<String> createDocument(CreateDocumentRequest dto) {
        return commandGateway.send(new CreateDocumentCommand(UUID.randomUUID().toString(), dto.getName()));
    }

    @Override
    public void deleteDocument(String documentId) {
        commandGateway.send(new DeleteDocumentCommand(documentId)).join();
    }

    @Override
    public CompletableFuture<Void> appendLine(String id, DocumentLineDto line) {
        return commandGateway.send(new AppendLineCommand(id, line.getText()));
    }

    @Override
    public CompletableFuture<Void> updateLine(String id, DocumentLineDto line) {
        return commandGateway.send(new UpdateLineCommand(id, line.getLineNumber(), line.getText()));
    }

    @Override
    public CompletableFuture<Void> insertLine(String id, DocumentLineDto line) {
        return commandGateway.send(new InsertLineCommand(id, line.getLineNumber(), line.getText()));
    }

    @Override
    public CompletableFuture<Void> removeLine(String id, DocumentLineDto line) {
        return commandGateway.send(new RemoveLineCommand(id, line.getLineNumber()));
    }

    @Override
    public void undo(String id) {
        commandGateway.send(new UndoCommand(id)).join();
    }


    @Override
    public List<AggregateHistoryDTO> getHistory(String id) {
        List<AggregateHistoryDTO> events = eventStore
                .readEvents(id)
                .asStream()
                .map(this::domainEventToAggregateHistory)
                .collect(Collectors.toList());

        return events;
    }

    private AggregateHistoryDTO domainEventToAggregateHistory(DomainEventMessage<?> event)
    {
        return new AggregateHistoryDTO(event.getPayloadType().getSimpleName(), (DocumentEvent) event.getPayload(), event.getSequenceNumber(),
                event.getTimestamp());
    }


}
