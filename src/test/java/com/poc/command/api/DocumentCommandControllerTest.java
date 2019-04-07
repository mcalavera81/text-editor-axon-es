package com.poc.command.api;

import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import com.poc.command.application.DocumentCommandService;
import com.poc.command.dto.CreateDocumentRequest;
import com.poc.command.dto.DocumentLineDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DocumentCommandControllerTest {

    @Value("${local.server.port}")
    private int port;

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    private String docId;

    @Autowired
    private DocumentCommandService commandService;

    @Before
    public void testCreateDocument() {
        String documentUrl = baseUrl("/document");

        String docName = "docName";
        CreateDocumentRequest request = new CreateDocumentRequest(docName);

        // @formatter:off

        docId = given().
            filter(new RequestLoggingFilter()).
            filter(new ResponseLoggingFilter()).
            body(request).
            contentType(MediaType.APPLICATION_JSON_VALUE).
        when().
            post(documentUrl).
        then().
            statusCode(HttpStatus.OK.value()).
        extract().
            path("id");


        // @formatter:on

        assertNotNull(docId);
    }

    @Test
    public void testAppendLine() {

        String docId = commandService.createDocument(new CreateDocumentRequest("docName")).join();

        String documentUrl = baseUrl("/document");


        // @formatter:off

        given().
            filter(new RequestLoggingFilter()).
            filter(new ResponseLoggingFilter()).
            body(DocumentLineDto.builder().text("My Line").build()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            pathParam("docId",docId).
        when().
            post(documentUrl+"/{docId}/append").
        then().
            statusCode(HttpStatus.OK.value());

        // @formatter:on
    }

    @Test
    public void testUpdateLine() {

        String docId = commandService.createDocument(new CreateDocumentRequest("docName")).join();
        commandService.appendLine(docId, DocumentLineDto.builder().text("insertedText 1").build() );

        String documentUrl = baseUrl("/document");


        // @formatter:off

        given().
            filter(new RequestLoggingFilter()).
            filter(new ResponseLoggingFilter()).
            body(DocumentLineDto.builder().text("My Line updated").lineNumber(1).build()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            pathParam("docId",docId).
        when().
            post(documentUrl+"/{docId}/update").
        then().
            statusCode(HttpStatus.OK.value());

        // @formatter:on
    }

    @Test
    public void testUpdateNonExistentLine() {

        String docId = commandService.createDocument(new CreateDocumentRequest("docName")).join();
        commandService.appendLine(docId, DocumentLineDto.builder().text("insertedText 1").build() );

        String documentUrl = baseUrl("/document");


        // @formatter:off

        given().
            filter(new RequestLoggingFilter()).
            filter(new ResponseLoggingFilter()).
            body(DocumentLineDto.builder().text("My Line updated").lineNumber(2).build()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            pathParam("docId",docId).
        when().
            post(documentUrl+"/{docId}/update").
        then().
            statusCode(HttpStatus.BAD_REQUEST.value());

        // @formatter:on
    }

    @Test
    public void testInsertLine() {

        String docId = commandService.createDocument(new CreateDocumentRequest("docName")).join();
        commandService.appendLine(docId, DocumentLineDto.builder().text("insertedText 1").build() );

        String documentUrl = baseUrl("/document");


        // @formatter:off

        given().
            filter(new RequestLoggingFilter()).
            filter(new ResponseLoggingFilter()).
            body(DocumentLineDto.builder().lineNumber(1).text("My Line inserted").build()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            pathParam("docId",docId).
        when().
            post(documentUrl+"/{docId}/insert").
        then().
            statusCode(HttpStatus.OK.value());

        // @formatter:on
    }

    @Test
    public void testInsertNonExistentLine() {

        String docId = commandService.createDocument(new CreateDocumentRequest("docName")).join();
        commandService.appendLine(docId, DocumentLineDto.builder().text("insertedText 1").build() );

        String documentUrl = baseUrl("/document");


        // @formatter:off

        given().
            filter(new RequestLoggingFilter()).
            filter(new ResponseLoggingFilter()).
            body(DocumentLineDto.builder().lineNumber(2).text("My Line inserted").build()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            pathParam("docId",docId).
        when().
            post(documentUrl+"/{docId}/insert").
        then().
            statusCode(HttpStatus.BAD_REQUEST.value());

        // @formatter:on
    }

    @Test
    public void testRemoveLine() {

        String docId = commandService.createDocument(new CreateDocumentRequest("docName")).join();
        commandService.appendLine(docId, DocumentLineDto.builder().text("insertedText 1").build() );

        String documentUrl = baseUrl("/document");


        // @formatter:off

        given().
            filter(new RequestLoggingFilter()).
            filter(new ResponseLoggingFilter()).
            body(DocumentLineDto.builder().lineNumber(1).build()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            pathParam("docId",docId).
        when().
            post(documentUrl+"/{docId}/remove").
        then().
            statusCode(HttpStatus.OK.value());

        // @formatter:on
    }

    @Test
    public void testRemoveNonExistentLine() {

        String docId = commandService.createDocument(new CreateDocumentRequest("docName")).join();
        commandService.appendLine(docId, DocumentLineDto.builder().text("insertedText 1").build() );

        String documentUrl = baseUrl("/document");


        // @formatter:off

        given().
            filter(new RequestLoggingFilter()).
            filter(new ResponseLoggingFilter()).
            body(DocumentLineDto.builder().lineNumber(2).build()).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            pathParam("docId",docId).
        when().
            post(documentUrl+"/{docId}/remove").
        then().
            statusCode(HttpStatus.BAD_REQUEST.value());

        // @formatter:on
    }
}