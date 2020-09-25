/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.theoretical.basis;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * StarvationThreadAndLiveLockDemo
 * 线程饥饿 和 活锁
 * https://docs.oracle.com/javase/tutorial/essential/concurrency/starvelive.html
 *
 * @author guoxing
 * @date 2020/9/25 4:38 PM
 * @since
 */
@Slf4j
public class StarvationThreadAndLiveLockDemo {
    public static void main(String[] args) {
        // 线程饥饿表示的是线程过长时间不能访问到共享资源
        for (int i = 0; i < 2; i++) {
            new Thread(StarvationThreadAndLiveLockDemo::longTimeReturn).start();
        }
        liveLock();
    }

    // 当前仅且只有一个线程能够进入,对于其他被阻塞的线程过长时间都需要等待,因此其他线程被称为线程饥饿
    public static synchronized void longTimeReturn() {
        log.info("当前线程:{}进入", Thread.currentThread().getName());
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // 在得不到想要的资源时,会释放当前占有资源所有权,导致的结果是双方互换自己手中的资源,但并不能同时获得到全部的资源
    public static void liveLock() {
        ReentrantLock r1 = new ReentrantLock();
        ReentrantLock r2 = new ReentrantLock();
        new Thread(() -> {
            String name = Thread.currentThread().getName();
            while (true) {
                if (r1.tryLock()) {
                    log.info("线程:{}获取到r1", name);
                    if (r2.tryLock()) {
                        log.info("线程:{}获取到r2", name);
                        r2.unlock();
                        log.info("线程:{}释放r2", name);
                        break;
                    } else {
                        r1.unlock();
                        log.info("线程:{}释放r1", name);
                    }
                }
            }
            log.info("线程:{}执行结束", name);
        }).start();
        new Thread(() -> {
            String name = Thread.currentThread().getName();
            while (true) {
                if (r2.tryLock()) {
                    log.info("线程:{}获取到r2", name);
                    if (r1.tryLock()) {
                        log.info("线程:{}获取到r1", name);
                        r1.unlock();
                        log.info("线程:{}释放r1", name);
                        break;
                    } else {
                        r2.unlock();
                        log.info("线程:{}释放r2", name);
                    }
                }
            }
            log.info("线程:{}执行结束", name);
        }).start();
    }
}
