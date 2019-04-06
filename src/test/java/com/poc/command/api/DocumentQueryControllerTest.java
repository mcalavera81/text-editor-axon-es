package com.poc.command.api;

import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import com.poc.command.application.DocumentCommandService;
import com.poc.command.dto.CreateDocumentRequest;
import com.poc.command.dto.DocumentLineDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DocumentQueryControllerTest {

    @Value("${local.server.port}")
    private int port;

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }


    @Autowired
    private DocumentCommandService commandService;

    @Test
    public void test() throws ExecutionException, InterruptedException {

        String docId = commandService.createDocument(new CreateDocumentRequest("docName")).join();

        String[] lines = {"insertedText 1","insertedText 2","insertedText 3"};
        commandService.appendLine(docId, DocumentLineDto.builder().text(lines[0]).build() );
        commandService.appendLine(docId, DocumentLineDto.builder().text(lines[1]).build());
        commandService.appendLine(docId, DocumentLineDto.builder().text(lines[2]).build());


        String documentUrl = baseUrl("/document");


        // @formatter:off

        Integer numberOfLines = given().
            filter(new RequestLoggingFilter()).
            filter(new ResponseLoggingFilter()).
            pathParam("id", docId).
        when().
            get(documentUrl+"/{id}/size").
        then().
            statusCode(HttpStatus.OK.value()).
        extract().
            path("numberOfLines");


        assertThat(numberOfLines).isEqualTo(3);



        String text = given().
            filter(new RequestLoggingFilter()).
            filter(new ResponseLoggingFilter()).
            pathParam("id", docId).
        when().
            get(documentUrl+"/{id}/text").
        then().
            statusCode(HttpStatus.OK.value()).
        extract().
            path("text");

        assertThat(text).isEqualTo(String.join("\n",lines));
    }
}