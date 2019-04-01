package com.poc.command.application;

import com.poc.command.dto.CreateDocumentRequest;

import java.util.concurrent.CompletableFuture;

public interface DocumentCommandService {

    CompletableFuture<String> createDocument(CreateDocumentRequest accountCreateDTO);
    CompletableFuture<String> deleteDocument(String documentId);
    CompletableFuture<String> appendLine(String id, String line);
    CompletableFuture<Void> updateLine(String id, Integer number, String line);

}
