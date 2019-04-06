package com.poc.command.dto;

import lombok.*;

@Data
@Builder
public class DocumentLineDto {
    private Integer lineNumber;
    private String text;

}
