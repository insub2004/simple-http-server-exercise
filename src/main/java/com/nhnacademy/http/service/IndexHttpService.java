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

package com.nhnacademy.http.service;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class IndexHttpService implements HttpService{
    /*TODO#2 /index.html을  처리하는 HttpService 입니다.
        - doGet()method를 구현 합니다.
    */

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        throw new RuntimeException("POST NOT ALLOW");
    }

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {

        //Body-설정
        //Header-설정
        String responseBody;
        try {
            responseBody = ResponseUtils.tryGetBodyFromFile(httpRequest.getRequestURI());
            log.debug("doGet responseBody : {}", responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String responseHeader = ResponseUtils.createResponseHeader(ResponseUtils.HttpStatus.OK.getCode(), "utf-8", responseBody.getBytes(StandardCharsets.UTF_8).length);

        //PrintWriter 응답
        try(PrintWriter bufferedWriter = httpResponse.getWriter()){
            bufferedWriter.write(responseHeader);
            bufferedWriter.write(responseBody);
            bufferedWriter.flush();
            log.debug("flush! body:{}", responseBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
