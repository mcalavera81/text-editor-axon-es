package com.poc.command.domain;


import com.poc.command.event.DocumentCreatedEvent;
import com.poc.command.event.DocumentDeletedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class DocumentAggregateTest {

    private FixtureConfiguration<DocumentAggregate> fixture;


    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(DocumentAggregate.class);
    }


    @Test
    public void givenNoPriorActivity_whenCreateDocumentCommand_thenShouldPublishDocumentCreatedEvent() {
        String id = UUID.randomUUID().toString();
        String name= "Document 1";
        fixture.givenNoPriorActivity()
                .when(new CreateDocumentCommand(id, name))
                .expectEvents(new DocumentCreatedEvent(id, name));
    }

    @Test
    public void givenDocumentCreatedEvent_whenDeleteDocumentCommand_thenShouldPublishDocumentDeletedEvent() {
        String id = UUID.randomUUID().toString();
        String name= "Document 1";
        fixture.given(new DocumentCreatedEvent(id, name))
                .when(new DeleteDocumentCommand(id))
                .expectEvents(new DocumentDeletedEvent(id));
    }


}