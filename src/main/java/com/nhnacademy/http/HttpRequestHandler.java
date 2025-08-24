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

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
/* TODO#6 Java에서 Thread는 implements Runnable or extends Thread를 이용해서 Thread를 만들 수 있습니다.
*  implements Runnable을 사용하여 구현 합니다.
*/
public class HttpRequestHandler implements Runnable{
    private final Socket client;

    private final static String CRLF="\r\n";

    public HttpRequestHandler(Socket client) {
        //TODO#7 생성자를 초기화 합니다., cleint null or socket close 되었다면 적절히 Exception을 발생시킵니다.
        if (Objects.isNull(client) || client.isClosed()) {
            throw new IllegalArgumentException("invalid client");
        }
        this.client = client;
    }

    @Override
    public void run() {
        //TODO#8 exercise-simple-http-server-step1을 참고 하여 구현 합니다.
        log.debug("[thread : {}] start", Thread.currentThread().getName());

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter writer = new PrintWriter(client.getOutputStream(),false, StandardCharsets.UTF_8)
        ) {

            while (true) {
                String line = br.readLine();
                log.debug("request : {}", line);

                if (Objects.isNull(line) || line.length() == 0) {
                    //종료 조건 null or line.length==0
                    break;
                }
            }

            // 응답 헤더, 응답 바디
            StringBuilder responseHeader = new StringBuilder();
            // HTML 내용
            StringBuilder responseBody = new StringBuilder();

            responseBody.append("<h1>");
            responseBody.append("<body>");
            responseBody.append("<h1>hello java(step2)</h1>");
            responseBody.append("</body>");
            responseBody.append("</h1>");

            // 응답 헤더 내용 추가
            responseHeader.append("HTTP/1.1 200 OK%s".formatted(CRLF));
            responseHeader.append(String.format("Server: HTTP server/0.1%s",CRLF));
            responseHeader.append(String.format("Content-type: text/html; charset=%s%s","UTF-8",CRLF));
            //responseHeader.append(String.format("Connection: Closed%s",CRLF));
            responseHeader.append(String.format("Content-Length:%d %s%s",responseBody.length(),CRLF,CRLF));

            writer.write(responseHeader.toString());
            writer.write(responseBody.toString());
            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.debug("[thread : {}] end", Thread.currentThread().getName());
    }
}
