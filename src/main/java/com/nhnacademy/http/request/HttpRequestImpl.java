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

package com.nhnacademy.http.request;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class HttpRequestImpl implements HttpRequest {
    /* TODO#2 HttpRequest를 구현 합니다.
    *  test/java/com/nhnacademy/http/request/HttpRequestImplTest TestCode를 실행하고 검증 합니다.
    */

    private final Socket client;

    private final Map<String, Object> headerMap = new HashMap<>();
    private final Map<String, Object> attributeMap = new HashMap<>();
    private final String REQUEST_METHOD = "REQUEST_METHOD";
    private final String PARAMETER_MAP = "PARAMETER_MAP";
    private final String REQUEST_URL = "REQUEST_URL";


    public HttpRequestImpl(Socket client) {
        this.client = client;
        init();
    }

    // 요청을 일단 다 받아서 Map 필드에 저장하자
    private void init() {
        StringBuilder sb = new StringBuilder();

        try {       // try - catch - resource 문 함부로 쓰지마셈 ㅋㅋ
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("GET") || line.contains("POST")) {  // 첫 줄
                    String[] strings = line.split(" ");
                    // method 추가
                    headerMap.put(REQUEST_METHOD, strings[0]);
                    if (strings[1].contains("?")) {     // 파라미터 있는 요청
                        // params 추가
                        // 처음"?"부터 마지막" "까지 짤라서 -> 구분자 &로 나눔(key=value) -> 구분자 =로 나눠서 맵에 넣기
                        String[] parameterSection = strings[1].split("\\?");
                        // 요청 url 저장
                        HashMap<String, Object> paramsMap = new HashMap<>();
                        headerMap.put(REQUEST_URL, parameterSection[0]);
                        String[] parameters = parameterSection[1].split("&");
                        for (String parameter : parameters) {
                            String[] param = parameter.split("=");
                            paramsMap.put(param[0], param[1]);      // "id"=marco
                        }
                        headerMap.put(PARAMETER_MAP, paramsMap);
                    } else {
                        headerMap.put(REQUEST_URL, strings[1]);     // 파라미터 없는 요청
                    }

                } else if (Objects.isNull(line) || line.isEmpty()) {  // 마지막 줄
                    break;
                } else {
                    // 첫줄 과 마지막줄을 제외한 나머지 헤더값 저장
                    // 구분자 ":"
                    // Host의 경우 "Host: localhost:8080%" 이지만 일단 localhost저장한다 가정
                    String[] strings = line.split(":");
                    log.debug(strings[0] + ":" + strings[1].trim());
                    headerMap.put(strings[0], strings[1].trim());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getMethod() {
        return String.valueOf(headerMap.get(REQUEST_METHOD));
    }

    @Override
    public String getParameter(String name) {
        return String.valueOf(getParameterMap().get(name));
    }

    @Override
    public Map<String, String> getParameterMap() {
        return (Map<String, String>) headerMap.get(PARAMETER_MAP);
    }

    @Override
    public String getHeader(String name) {
        return String.valueOf(headerMap.get(name));
    }

    @Override
    public void setAttribute(String name, Object o) {
        attributeMap.put(name, o);
    }

    @Override
    public Object getAttribute(String name) {
        return attributeMap.get(name);
    }

    @Override
    public String getRequestURI() {
        return String.valueOf(headerMap.get(REQUEST_URL));
    }
}
