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

package com.nhnacademy;

import com.nhnacademy.http.SimpleHttpServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class App 
{
    public static void main( String[] args ){
        SimpleHttpServer simpleHttpServer = new SimpleHttpServer();
        //simpleHttpServer.start();

        String str1 = null;
        String str2 = "";
        String str3 = "Hello";

        System.out.println(isNullOrEmpty(str1)); // true
        System.out.println(isNullOrEmpty(str2)); // true
        System.out.println(isNullOrEmpty(str3)); // false
    }


    public static boolean isNullOrEmpty(String str) {
        return StringUtils.isEmpty(str);
    }
}
