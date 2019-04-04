package com.poc.command.application;

import com.poc.command.dto.CreateDocumentRequest;
import com.poc.query.domain.repository.dto.AggregateHistoryDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DocumentCommandService {

    CompletableFuture<String> createDocument(CreateDocumentRequest accountCreateDTO);
    void deleteDocument(String documentId);
    void appendLine(String id, String line);
    void updateLine(String id, Integer number, String line);
    void undo(String id);

    List<AggregateHistoryDTO> getHistory(String id);

}
