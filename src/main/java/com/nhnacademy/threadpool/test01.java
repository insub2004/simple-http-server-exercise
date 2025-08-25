package com.nhnacademy.threadpool;

import java.net.ServerSocket;
import java.util.concurrent.*;

public class test01 {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            Future<?> submit = executor.submit(new Worker("worker" + i));
        }

        // 새 작업 거부, 기존 작업은 수행
        executor.shutdown();

        try {
            if (executor.awaitTermination(30, TimeUnit.SECONDS)) {      // 풀의 완전히 종료를 최대 timeout까지 대기
                executor.shutdownNow();                                         // 강제 종료 시도 (인터럽트)
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    // 여전히 안끝나면 로깅/경고
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
