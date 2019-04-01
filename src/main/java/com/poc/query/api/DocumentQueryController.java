package com.poc.query.api;

import com.poc.query.application.DocumentQueryService;
import com.poc.query.domain.repository.dto.DocumentViewDto;
import io.swagger.annotations.Api;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping(value = "/document")
@Api(value = "Document Queries", description = "Document Query-Related Endpoint", tags = "Document Queries")
public class DocumentQueryController {

    private final DocumentQueryService queryService;

    public DocumentQueryController(DocumentQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentViewDto> findDocument(@PathVariable String id) {
        return Optional
                .ofNullable(queryService.findDocument(id))
                .map(doc -> ResponseEntity.ok().body(doc) )
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
