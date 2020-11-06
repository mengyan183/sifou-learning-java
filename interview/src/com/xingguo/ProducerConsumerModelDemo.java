/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ProducerConsumerModelDemo
 * 生产者消费者模型
 * 利用条件变量 通知和等待
 *
 * @author guoxing
 * @date 2020/11/6 11:14 AM
 * @since 1.0.0
 */
public class ProducerConsumerModelDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        /**
         * 实现 两个线程顺序打印 1-100 中的数字,一个线程打印奇数,一个线程打印偶数
         */
        ReentrantLock reentrantLock = new ReentrantLock();
        // 奇数条件变量
        Condition oddCondition = reentrantLock.newCondition();
        // 偶数条件变量
        Condition evenCondition = reentrantLock.newCondition();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        // 奇数
        executorService.execute(() -> {
            while (atomicInteger.get() < 100) {
                reentrantLock.lock();
                while (atomicInteger.get() % 2 == 0) {
                    try {
                        oddCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                print(atomicInteger.getAndIncrement());
                evenCondition.signalAll();
                reentrantLock.unlock();
            }
        });

        // 偶数
        executorService.execute(() -> {
            while (atomicInteger.get() < 101) {
                reentrantLock.lock();
                while (atomicInteger.get() % 2 == 1) {
                    try {
                        evenCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                print(atomicInteger.getAndIncrement());
                oddCondition.signalAll();
                reentrantLock.unlock();
            }
        });
        executorService.shutdown();
    }

    public static void print(int i) {
        System.out.println(Thread.currentThread().getName() + " : " + i);
    }
}
