package com.poc.query;


import com.poc.command.application.DocumentCommandService;
import com.poc.command.dto.CreateDocumentRequest;
import com.poc.command.dto.DocumentLineDto;
import com.poc.query.application.DocumentQueryService;
import com.poc.query.domain.repository.dto.DocumentViewDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DocumentQueryServiceTest {

    @Autowired
    private DocumentCommandService commandService;

    @Autowired
    private DocumentQueryService queryService;

    @Test
    public void testQueryModel() throws ExecutionException, InterruptedException {

        String docId = commandService.createDocument(new CreateDocumentRequest("docName")).join();

        String[] lines = {"appendedText 1", "appendedText 2", "appendedText 3"};
        commandService.appendLine(docId, DocumentLineDto.builder().text(lines[0]).build());
        commandService.appendLine(docId, DocumentLineDto.builder().text(lines[1]).build());
        commandService.appendLine(docId, DocumentLineDto.builder().text(lines[2]).build());
        commandService.updateLine(docId, DocumentLineDto.builder().lineNumber(2).text("new Line 2").build());
        commandService.insertLine(docId, DocumentLineDto.builder().lineNumber(2).text("insertedLine 2").build());

        Thread.sleep(1000);
        DocumentViewDto document = queryService.findDocument(docId);
        System.out.println(document.getLines());
        assertThat(document.getLine(2)).isEqualTo("insertedLine 2");
        assertThat(document.getLine(3)).isEqualTo("new Line 2");

        commandService.undo(docId); //Undo insert
        Thread.sleep(1000);
        document = queryService.findDocument(docId);
        assertThat(document.getLine(2)).isEqualTo("new Line 2");

        commandService.undo(docId); //Undo update
        Thread.sleep(1000);
        document = queryService.findDocument(docId);
        assertThat(document.getLine(2)).isEqualTo("appendedText 2");
        assertThat(document.getLines().size()).isEqualTo(3);

        commandService.undo(docId); //Undo append
        Thread.sleep(1000);
        document = queryService.findDocument(docId);
        assertThat(document.getLines().size()).isEqualTo(2);

        commandService.undo(docId);
        Thread.sleep(1000);
        document = queryService.findDocument(docId);
        assertThat(document.getLines().size()).isEqualTo(1);

        commandService.undo(docId);
        Thread.sleep(1000);
        document = queryService.findDocument(docId);
        assertThat(document.getLines().size()).isEqualTo(0);

    }

}