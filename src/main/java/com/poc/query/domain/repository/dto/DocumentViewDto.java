package com.poc.query.domain.repository.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
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
    private List<String> lines;


    @JsonGetter
    public String getId() {return id;}

    @JsonGetter
    public String getName() {
        return name;
    }

    @JsonGetter
    public Integer getNumberOfLines(){
        return lines.size();
    }

    public String getLine(int lineNumber){
        if(lineNumber <= 0) throw new IllegalArgumentException("lineNumber <= 0");

        return lines.get(lineNumber-1);
    }

    public List<String> getLines() {return lines;}

    public String getText() {
        return String.join("\n",lines);
    }

    public void appendLine(String line){
        lines.add(line);
    }

    public void updateLine(Integer number, String line){
        lines.set(number-1,line);
    }
}
