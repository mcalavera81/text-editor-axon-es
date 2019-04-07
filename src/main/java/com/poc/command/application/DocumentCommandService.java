package com.poc.command.application;

import com.poc.command.dto.CreateDocumentRequest;
import com.poc.command.dto.DocumentLineDto;
import com.poc.query.domain.repository.dto.AggregateHistoryDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DocumentCommandService {

    CompletableFuture<String> createDocument(CreateDocumentRequest accountCreateDTO);
    void deleteDocument(String documentId);
    CompletableFuture<Void> appendLine(String id, DocumentLineDto line);
    CompletableFuture<Void> updateLine(String id, DocumentLineDto line);
    CompletableFuture<Void> insertLine(String id, DocumentLineDto line);
    CompletableFuture<Void> removeLine(String id, DocumentLineDto line);
    void undo(String id);

    List<AggregateHistoryDTO> getHistory(String id);

}
