package com.poc.command.api;


import com.poc.command.application.DocumentCommandService;
import com.poc.command.dto.CreateDocumentRequest;
import com.poc.command.dto.CreateDocumentResponse;
import com.poc.command.dto.DocumentLineDto;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/document")
@Api(value = "Document Commands", description = "Document Command-Related Endpoint", tags = "Document Commands")
public class DocumentCommandController {


    private final DocumentCommandService commandService;

    public DocumentCommandController(DocumentCommandService commandService) {
        this.commandService = commandService;
    }


    @PostMapping
    public CompletableFuture<CreateDocumentResponse> createDocument(@RequestBody CreateDocumentRequest dto){
        return commandService.createDocument(dto).thenApply(CreateDocumentResponse::new);
    }

    @PostMapping(value = "/{docId}/append",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void appendLine(@PathVariable  String docId, @RequestBody DocumentLineDto line){
        commandService.appendLine(docId, line);
    }

    @PostMapping(value = "/{docId}/update",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateLine(@PathVariable  String docId, @RequestBody DocumentLineDto line){
        commandService.updateLine(docId, line);
    }

    @PostMapping(value = "/{docId}/insert",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insertLine(@PathVariable  String docId, @RequestBody DocumentLineDto line){
        commandService.insertLine(docId, line);
    }

    @PostMapping(value = "/{docId}/remove",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void removeLine(@PathVariable  String docId, @RequestBody DocumentLineDto line){
        commandService.removeLine(docId, line);
    }

    @PostMapping(value = "/{docId}/undo",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void undo(@PathVariable  String docId){
        commandService.undo(docId);
    }


    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable  String id){
        commandService.deleteDocument(id);
    }
}
