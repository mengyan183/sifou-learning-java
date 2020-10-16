/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * AcquireAndReleaseDemo
 * 锁获取和释放
 *
 * @author guoxing
 * @date 2020/9/26 9:32 AM
 * @since
 */
@Slf4j
public class AcquireAndReleaseDemo {
    public static void main(String[] args) {
        //锁的机制
        // acquire 获取
        // 线程进入同步代码块执行加锁
        // release 释放
        //1:当线程持有锁时,调用Object#wait
        //2:Thread#park 实际调用的是 LockSupport#park ,线程挂起
        //3:Condition#await
        // 4:当前持有锁的线程消亡
        //5:java9 自旋锁 Thread.onSpinWait
        //6:Thread.yield
        Object o = new Object();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            executorService.submit(() -> {
                synchronized (o) {
                    log.info("{}获取到锁", Thread.currentThread().getName());
                    try {
                        countDownLatch.countDown();
                        o.wait();// 当执行wait操作时会将当前线程挂起,此时o属于 lock free状态,因此其他的线程都可以竞争到锁
                        log.info("{}被唤醒",Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        // 保证三个线程都被挂起
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (o){
            o.notifyAll();// 会一直对 waitSet中的节点进行唤醒操作,且重新获取到 对象o 的所有权,因此上述的代码才会在加锁保护下继续执行结束
        }
        executorService.shutdown();

        //锁的公平和非公平
        // 公平(fair) 表示的是在等待队列中的线程 FIFO
        //不公平(unfair) 等待线程随机调度
        // 在创建线程时尽量不要指定线程的优先级
    }
}
