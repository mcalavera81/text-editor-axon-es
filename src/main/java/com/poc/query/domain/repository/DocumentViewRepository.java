package com.poc.query.domain.repository;

import com.poc.query.domain.repository.dto.DocumentViewDto;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface DocumentViewRepository extends MongoRepository<DocumentViewDto, String> {

}