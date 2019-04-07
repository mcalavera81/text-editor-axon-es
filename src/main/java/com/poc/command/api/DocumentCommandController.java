package com.poc.command.api;


import com.poc.command.application.DocumentCommandService;
import com.poc.command.dto.CommandDto;
import com.poc.command.dto.CreateDocumentRequest;
import com.poc.command.dto.CreateDocumentResponse;
import com.poc.command.dto.DocumentLineDto;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public CompletableFuture<ResponseEntity<CommandDto>> appendLine(@PathVariable  String docId, @RequestBody DocumentLineDto line){
        return commandService
                .appendLine(docId, line).thenApply(o -> ResponseEntity.ok().body(new CommandDto()))
                .exceptionally(e -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommandDto(e.getMessage())));
    }

    @PostMapping(value = "/{docId}/update",consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<CommandDto>> updateLine(@PathVariable  String docId, @RequestBody DocumentLineDto line){
        return commandService
                .updateLine(docId, line).thenApply(o -> ResponseEntity.ok().body(new CommandDto()))
                .exceptionally(e -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommandDto(e.getMessage())));

    }

    @PostMapping(value = "/{docId}/insert",consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<CommandDto>>  insertLine(@PathVariable  String docId, @RequestBody DocumentLineDto line){
        return commandService
                .insertLine(docId, line).thenApply(o -> ResponseEntity.ok().body(new CommandDto()))
                .exceptionally(e -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommandDto(e.getMessage())));

    }

    @PostMapping(value = "/{docId}/remove",consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<CommandDto>>  removeLine(@PathVariable  String docId, @RequestBody DocumentLineDto line){
        return commandService
                .removeLine(docId, line).thenApply(o -> ResponseEntity.ok().body(new CommandDto()))
                .exceptionally(e -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommandDto(e.getMessage())));
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
