/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.theoretical.basis;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ProducerAndConsumerDemo
 * 生产者消费者模型
 *
 * @author guoxing
 * @date 2020/9/25 4:56 PM
 * @since
 */
@Slf4j
public class ProducerAndConsumerDemo {

    public static void main(String[] args) {

        Container container = new Container();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Runnable producer = () -> {
            try {
                container.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Runnable consumer = () -> {
            try {
                container.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        // 创建两个异步任务
        CompletableFuture.allOf(CompletableFuture.runAsync(producer, executorService), CompletableFuture.runAsync(consumer, executorService)).join();
//        executorService.submit(producer);
//        executorService.submit(consumer);
        executorService.shutdown();

    }

    public static class Container {
        private final int MAX_SIZE = 5;

        private List<Integer> data = new LinkedList<>();
        private Random random = new Random();

        public void produce() throws InterruptedException {
            while (true) {
                int i = random.nextInt(100);
                synchronized (this) {
                    while (MAX_SIZE <= data.size()) {
                        wait();
                    }
                    data.add(i);
                    log.info("生产者写入数据:{}", i);
                    notify(); // 通知消费者去消费
                }
                Thread.sleep(random.nextInt(1000));
            }
        }

        public void consume() throws InterruptedException {
            while (true) {
                synchronized (this) {
                    while (data.isEmpty()) {
                        wait();
                    }
                    log.info("消费者数据:{}", data.remove(0));
                    notify(); // 通知生产者
                }
                Thread.sleep(random.nextInt(1000));
            }
        }
    }
}
