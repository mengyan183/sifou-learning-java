/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.gc.optimization;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassStaticPropertyDemo
 *
 * @author guoxing
 * @date 2020/11/7 4:38 PM
 * @since
 */
@Slf4j
public class ClassStaticPropertyDemo {
    public static Integer i;

    public static void main(String[] args) {
        Random e = new Random();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            for (int j = 0; j < 10; j++) {
                log.info("{}", i);
                i = e.nextInt();
                log.info("{}", i);
            }
        });
        executorService.execute(() -> {
            for (int j = 0; j < 10; j++) {
                log.info("{}", i);
                i = e.nextInt();
                log.info("{}", i);
            }
        });
        executorService.shutdown();


    }

}
