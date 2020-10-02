/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CompletableFutureDemo
 *
 * @author guoxing
 * @date 2020/10/2 11:36 AM
 * @since
 */
@Slf4j
public class CompletableFutureDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CompletableFuture.allOf(CompletableFuture.runAsync(() -> {
            int i = new Random().nextInt(10);
            for (int j = 0; j < i; j++) {
                log.info("{}", j);
            }
        }, executorService), CompletableFuture.runAsync(() -> {
            int i = new Random().nextInt(10);
            for (int j = 0; j < i; j++) {
                log.info("{}", j);
            }
        }, executorService)).exceptionallyAsync(e -> {
            log.error(e.getMessage());
            return null;
        }, executorService).join();
        executorService.shutdown();
    }
}
