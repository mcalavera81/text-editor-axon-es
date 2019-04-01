package com.poc.query.application;

import com.poc.query.domain.repository.dto.DocumentViewDto;

public interface DocumentQueryService {

    DocumentViewDto findDocument(String id);

}
