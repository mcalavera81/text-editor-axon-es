package com.poc.command.api;


import com.poc.command.application.DocumentCommandService;
import com.poc.command.dto.CreateDocumentRequest;
import com.poc.command.dto.CreateDocumentResponse;
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

    @PostMapping(value = "/{id}/append",consumes = MediaType.TEXT_PLAIN_VALUE)
    public void appendLine(@PathVariable  String id, @RequestBody String line){
        commandService.appendLine(id, line);
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable  String id){
        commandService.deleteDocument(id);
    }
}
