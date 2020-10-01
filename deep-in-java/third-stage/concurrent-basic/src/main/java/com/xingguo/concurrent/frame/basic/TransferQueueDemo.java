/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;

/**
 * TransferQueueDemo
 *
 * @author guoxing
 * @date 2020/10/1 8:49 AM
 * @since
 */
@Slf4j
public class TransferQueueDemo {
    public static void main(String[] args) {
        LinkedTransferQueue<Integer> integers = new LinkedTransferQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            int i = 0;
            while (i < 100) {
                try {
                    integers.transfer(i);
                    log.info("写入数据:{}", i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        });
        // 当读取数量少于写入数量时,写入线程会被阻塞, 但数据读取保证了数据的顺序性
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    Integer take = integers.take();
                    log.info("读取数据:{}", take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        // 当不存在消费者时,最多只会写入一条数据
        while (true) {
            log.info("数量:{}", integers.size());
        }
    }
}
