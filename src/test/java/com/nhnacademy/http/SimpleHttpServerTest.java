/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2024. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package com.nhnacademy.http;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SimpleHttpServerTest {

    static Thread thread;
    static final int TEST_PORT = 9999;

    @BeforeAll
    static void beforeAllSetUp(){
        thread = new Thread(()->{
            SimpleHttpServer simpleHttpServer = new SimpleHttpServer(TEST_PORT);
            simpleHttpServer.start();
        });
        thread.start();
    }

    @Test
    @Order(1)
    @DisplayName("threadB - 홀수요청")
    void requestEvenNumber() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("http://localhost:%d",TEST_PORT)))
                .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        log.debug("response:{}",response.body());

        //TODO#105 threadB 문자열이 포함되었는지 검증 합니다.
        Assertions.assertTrue(response.body().toString().toLowerCase().contains("threadb"));
    }

    @Test
    @Order(2)
    @DisplayName("threadB - 짝수요청")
    void requestOddNumber() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("http://localhost:%d", TEST_PORT)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.debug("response:{}",response.body());

        //TODO#106 threadA 문자열이 포함되었는지 검증 합니다.
        Assertions.assertTrue(response.body().toString().toLowerCase().contains("threada"));
    }

    @Test
    @Order(3)
    @DisplayName("status code : 200 ok")
    void request1() throws URISyntaxException, IOException, InterruptedException {
        //TODO#107 response.statusCode()인지 검증 합니다.
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:%d".formatted(TEST_PORT))).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.debug("response:{}", response.body());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    @Order(4)
    @DisplayName("response: hello java")
    void request2() throws URISyntaxException, IOException, InterruptedException {
        //TODO#108 response.body()에 'hello' or 'java' 문자열이 포함되는지 검증 합니다.
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:%d".formatted(TEST_PORT))).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.debug("response:{}", response.body());
        Assertions.assertAll(
                () -> {
                    Assertions.assertTrue(response.body().toLowerCase().contains("java"));
                },
                () -> {
                    Assertions.assertTrue(response.body().toLowerCase().contains("hello"));
                }
        );
    }

    @Test
    @Order(5)
    @DisplayName("content-type")
    void request3() throws URISyntaxException, IOException, InterruptedException {
        //TODO#109 Content-Type header가 text/html 인지 검증 합니다.
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:%d".formatted(TEST_PORT))).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.debug("response : {}", response);
        log.debug("headers : {}", response.headers().toString());
        log.debug("{}",response.headers().firstValue("Content-Type").get());
        Optional<String> contentTypeOptional = response.headers().firstValue("Content-Type");
        String actual = "";
        if (contentTypeOptional.isPresent()) {
            actual = contentTypeOptional.get().toLowerCase();
        }
        log.debug("actual : {}", actual);
        Assertions.assertTrue(actual.contains("text/html"));
    }

    @Test
    @Order(6)
    @DisplayName("charset utf-8")
    void request4() throws URISyntaxException, IOException, InterruptedException {
        //TODO#110 charset이 utf-8인지 검증 합니다.
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:%d".formatted(TEST_PORT))).build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Optional<String> contentType = response.headers().firstValue("content-type");
        String actual = "";
        if (contentType.isPresent()) {
            actual = contentType.get().toLowerCase();
        }
        log.debug("contentType : {}", contentType);
        Assertions.assertTrue(actual.contains("utf-8"));
    }

    @AfterAll
    static void tearDown() throws InterruptedException {
       Thread.sleep(1000);
    }

}