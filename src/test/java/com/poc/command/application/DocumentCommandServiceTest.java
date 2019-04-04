package com.poc.command.application;

import com.poc.command.dto.CreateDocumentRequest;
import com.poc.command.event.UndoAppendedLineEvent;
import com.poc.command.event.UndoUpdatedLineEvent;
import com.poc.query.domain.repository.dto.AggregateHistoryDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DocumentCommandServiceTest {


    @Autowired
    private DocumentCommandService commandService;

    @Test
    public void testUndoAppend() throws ExecutionException, InterruptedException {

        String docId = commandService.createDocument(new CreateDocumentRequest("docName")).join();

        commandService.appendLine(docId, "line 1");

        List<AggregateHistoryDTO> history = commandService.getHistory(docId);
        assertThat(history).hasSize(2);


        commandService.undo(docId);
        history = commandService.getHistory(docId);
        assertThat(history).hasSize(3);

        assertThat(history.get(history.size()-1).getEvent()).isEqualTo(new UndoAppendedLineEvent(docId));

    }

    @Test
    public void testUndoUpdateLine() throws ExecutionException, InterruptedException {

        String docId = commandService.createDocument(new CreateDocumentRequest("docName")).join();
        commandService.appendLine(docId, "line 1");
        commandService.updateLine(docId, 1,"updated line 1");

        List<AggregateHistoryDTO> history = commandService.getHistory(docId);
        assertThat(history).hasSize(3);


        commandService.undo(docId);
        history = commandService.getHistory(docId);
        assertThat(history).hasSize(4);

        assertThat(history.get(history.size()-1).getEvent())
                .isEqualTo(new UndoUpdatedLineEvent(docId,1,"line 1"));

    }

}