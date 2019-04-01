package com.poc.query.domain.repository.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Document
public class DocumentViewDto {

    public DocumentViewDto(String id, String name) {
        this.id = id;
        this.name = name;
        this.lines = new LinkedList<>();
    }

    @Id
    private String id;
    private String name;

    public String getName() {
        return name;
    }

    private List<String> lines;

    public List<String> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public void appendLine(String line){
        lines.add(line);
    }

    public void updateLine(Integer number, String line){
        lines.set(number-1,line);
    }
}
