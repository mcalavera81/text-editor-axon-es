package com.poc.command.api;

import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import com.poc.command.dto.CreateDocumentRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    @Test
    public void test() {
        String documentUrl = baseUrl("/document");

        String docName = "docName";
        CreateDocumentRequest request = new CreateDocumentRequest(docName);

        // @formatter:off

        String id = given().
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

        assertNotNull(id);
    }
}