package com.poc.query.application;

import com.poc.query.domain.repository.dto.AggregateHistoryDTO;
import com.poc.query.domain.repository.dto.DocumentViewDto;
import org.axonframework.eventhandling.DomainEventMessage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DocumentQueryService {

    DocumentViewDto findDocument(String id);


}
