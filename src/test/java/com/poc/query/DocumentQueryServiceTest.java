package com.poc.query;


import com.poc.command.application.DocumentCommandService;
import com.poc.command.dto.CreateDocumentRequest;
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

        String[] lines = {"line 1", "line 2", "line 3"};
        commandService.appendLine(docId, lines[0]);
        System.out.println("-------------------");
        commandService.appendLine(docId, lines[1]);
        commandService.appendLine(docId, lines[2]);
        commandService.updateLine(docId, 2, "new Line 2");

        Thread.sleep(1000);
        DocumentViewDto document = queryService.findDocument(docId);
        assertThat(document.getLine(2)).isEqualTo("new Line 2");

        commandService.undo(docId);
        Thread.sleep(1000);
        document = queryService.findDocument(docId);
        assertThat(document.getLine(2)).isEqualTo("line 2");

        commandService.undo(docId);
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