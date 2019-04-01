package com.poc.query.domain.repository.dto;

import lombok.Builder;
import lombok.Value;
import lombok.val;

@Builder
@Value
public class ApiDto {

    private String id;
    private Integer numberOfLines;
    private String text;
}
