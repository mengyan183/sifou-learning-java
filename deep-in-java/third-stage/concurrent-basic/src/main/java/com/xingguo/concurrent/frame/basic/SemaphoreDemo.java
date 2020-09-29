/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * SemaphoreDemo
 *
 * @author guoxing
 * @date 2020/9/28 9:12 PM
 * @since
 */
@Slf4j
public class SemaphoreDemo {
    public static void main(String[] args) {

        final int MAX_VALUE = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(Byte.MAX_VALUE);
        // Semaphore 实际是存在令牌计数器的作用
        Semaphore semaphore = new Semaphore(MAX_VALUE);

        while (true) {
            executorService.submit(() -> {
                try {
                    log.info("当前线程:{}准备获取令牌", Thread.currentThread().getName());
                    semaphore.acquire();// 当不释放的情况下,只会有5个线程获取到令牌允许进入,其他线程都会被阻塞
                    log.info("当前线程:{}获取到令牌", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task();
                semaphore.release();
                log.info("当前线程:{}释放令牌", Thread.currentThread().getName());
            });
        }

    }

    public static void task() {
        log.info("当前线程:{}执行任务", Thread.currentThread().getName());
    }
}
