package com.poc.command.application;

import com.poc.command.domain.AppendLineCommand;
import com.poc.command.domain.CreateDocumentCommand;
import com.poc.command.domain.DeleteDocumentCommand;
import com.poc.command.domain.UpdateLineCommand;
import com.poc.command.dto.CreateDocumentRequest;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DefaultDocumentCommandService implements DocumentCommandService {

    private final CommandGateway commandGateway;

    @Override
    public CompletableFuture<String> createDocument(CreateDocumentRequest dto) {
        return commandGateway.send(new CreateDocumentCommand(UUID.randomUUID().toString(), dto.getName()));
    }

    @Override
    public CompletableFuture<String> deleteDocument(String documentId) {
        return commandGateway.send(new DeleteDocumentCommand(documentId));
    }

    @Override
    public CompletableFuture<String> appendLine(String id, String line) {
        return commandGateway.send(new AppendLineCommand(id, line));
    }

    @Override
    public CompletableFuture<Void> updateLine(String id, Integer number, String line) {
        return commandGateway.send(new UpdateLineCommand(id, number, line));
    }


}
