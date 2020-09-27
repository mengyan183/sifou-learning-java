/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * CountDownLathDemo
 *
 * @author guoxing
 * @date 2020/9/27 5:47 PM
 * @since
 */
@Slf4j
public class CountDownLathDemo {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个数量为5的计数器
        CountDownLatch countDownLatch = new CountDownLatch(5);
        ExecutorService executorService = new ThreadPoolExecutor(1, 3, 1L, TimeUnit.MINUTES, new SynchronousQueue<>(), new ThreadFactoryBuilder().setNameFormat("count_down_lath_demo-%d").build(), new ThreadPoolExecutor.CallerRunsPolicy());
        // 当实际执行次数小于计数器时,由于计数器一直不会等于0,因此当前线程在执行到" countDownLatch.await();" 会被阻塞
//        task(4, countDownLatch, executorService);
        // 当实际执行次数等于计数器时,当前代码会执行结束
//        task(5, countDownLatch, executorService);
        // 当实际执行次数大于计数器时,当前代码会执行结束,当countDownLath计数器归零时,实际countDownLath已没有任何效果
        task(6,countDownLatch,executorService);
        new Thread(()->{
           while (true){
               long count = countDownLatch.getCount();
               log.info("当前计数器为:{}",count);
               if(count==0){
                   break; // 由于进程中当前子线程未结束,所以当前整个进程也不会退出
               }
               try {
                   Thread.sleep(1000L);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        }).start();
        countDownLatch.await();// 当计数器大于0时,当前线程会被阻塞在此,原因在于,在调用java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(java.util.concurrent.locks.AbstractQueuedSynchronizer.Node, int, boolean, boolean, boolean, long)时由于计数器不归零,导致当前方法一直不会返回,因此才会阻塞当前线程
        executorService.shutdown();
        log.info("主线程结束");
    }

    public static void task(int count, CountDownLatch countDownLatch, ExecutorService executorService) {
        for (int i = 0; i < count; i++) {
            executorService.submit(() -> {
                log.info("线程:{}执行", Thread.currentThread().getName());
                // 计数器减1,当计数器为0时,实际内部代码并不会完整的执行,并不会再次修改计数器,因此计数器最小为0
                countDownLatch.countDown();
            });
        }
    }
}
