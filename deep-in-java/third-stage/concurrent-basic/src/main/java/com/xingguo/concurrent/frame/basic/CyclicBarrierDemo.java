/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.*;

/**
 * CyclicBarrierDemo
 *
 * @author guoxing
 * @date 2020/9/27 8:09 PM
 * @since
 */
@Slf4j
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        int count = 5;
        ExecutorService executorService = new ThreadPoolExecutor(1, 10, 1L, TimeUnit.MINUTES, new SynchronousQueue<>(), new ThreadFactoryBuilder().setNameFormat("cyclic_barrier_demo-%d").build(), new ThreadPoolExecutor.CallerRunsPolicy());
        Random random = new Random();
        // 将一个二维数组转换为一维数组
        int[][] rows = new int[count][count];
        int[] mergeRows = new int[2 * count];
        CyclicBarrier cyclicBarrier = new CyclicBarrier(count, () -> {
            // 可以起到方法回调的作用
            for (int j = 0; j < count; j++) {
                int[] firstRows = rows[j];
                int length = firstRows.length;
                for (int i = 0; i < length; i++) {
                    mergeRows[i + j] = firstRows[i];
                }
            }
            log.info("当前计数器已归零");
        });
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            // 对于第6个线程
            // 如果线程池中的最大线程数量小于计数器时,且拒绝策略为调用主线程时,会导致主线程被阻塞在await处,导致当前for循环不会继续执行,从而导致程序假死
            executorService.submit(() -> {
                log.info("线程:{}进入", Thread.currentThread().getName());
                for (int j = 0; j < count; j++) {
                    if (finalI < count) {
                        rows[finalI][j] = random.nextInt(10);
                    }
                }
                try {
                    // 当执行当前操作的线程数量不为计数器最大值的倍数时,则会存在异步线程被一直阻塞的情况,除非外部重置当前CyclicBarrier
                    cyclicBarrier.await();// 计数器不为0时,当线程执行到此处时,当前线程会被阻塞; 当计数器归为0时,会首先回调CyclicBarrier中的runnable,并唤醒当前所有被阻塞的线程
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                // 只有当计数器为0时,所有的异步线程才会执行到当前代码
                log.info("线程:{}执行结束", Thread.currentThread().getName());
            });
        }
        executorService.shutdown();
        Collections.singletonList(mergeRows).forEach(v -> {
            log.info("{}", v);
        });


        /**
         * 对比countDownLatch 和 cyclicBarrier
         * 1:都拥有计数器的功能
         * 2:对于CountDownLatch的主要功能一般是,异步线程执行,主线程阻塞; 而cyclicBarrier是异步线程阻塞,主线程执行(保证线程池的拒绝策略不是调用主线程,否则会导致主线程也被阻塞),且存在回调事件;
         * 3:CountDownLatch的计数器不支持重置,而cyclicBarrier支持计数器重置
         * 4:CountDownLatch可以获取到当前剩余计数,而cyclicBarrier不支持
         */
    }
}
