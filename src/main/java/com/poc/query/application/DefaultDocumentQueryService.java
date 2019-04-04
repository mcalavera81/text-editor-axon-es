package com.poc.query.application;

import com.poc.query.domain.repository.DocumentViewRepository;
import com.poc.query.domain.repository.dto.DocumentViewDto;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultDocumentQueryService implements DocumentQueryService {


    private final DocumentViewRepository repository;

    private final QueryGateway queryGateway;

    @Override
    public DocumentViewDto findDocument(String id) {
        //return queryGateway.query(new DocumentQuery(id), ResponseTypes.instanceOf(DocumentViewDto.class)).join();
        return repository.findById(id).orElse(null);
    }


}
