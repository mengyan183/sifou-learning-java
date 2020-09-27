/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLockDemo
 *
 * @author guoxing
 * @date 2020/9/26 9:51 AM
 * @since
 */
@Slf4j
public class ReentrantLockDemo {
    public static void main(String[] args) {
//        condition();
        getReentrantTime();
    }

    public static void getReentrantTime() {
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrant(reentrantLock, 10);
    }

    public static void reentrant(ReentrantLock reentrantLock, int times) {
        if (times <= 0) {
            return;
        }
        if (reentrantLock.tryLock()) {
            try {
                reentrant(reentrantLock, --times);
                log.info("第{}次当前线程对当前锁进入次数为:{}", times + 1, reentrantLock.getHoldCount());
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    /**
     * 通过condition实现线程通讯
     */
    public static void condition() {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        new Thread(() -> {
            while (true) {
                if (reentrantLock.tryLock()) {
                    try {
                        int waitQueueLength = reentrantLock.getWaitQueueLength(condition);
                        log.info("当前等待线程数量为:{}", waitQueueLength);
                        if (waitQueueLength > 10) {
                            condition.signalAll();
                        }
                    } finally {
                        reentrantLock.unlock();
                    }
                }
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (true) {
            new Thread(() -> {
                if (reentrantLock.tryLock()) {
                    log.info("当前线程id:{}", Thread.currentThread().getId());
                    try {
                        condition.await();
                        log.info("当前线程被唤醒:{}", Thread.currentThread().getId());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        reentrantLock.unlock();
                    }
                }
            }).start();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void synchronizedVsTryLock() {
        // 对于这种情况存在死锁的可能性,更多的可能性为线程饥饿,无法控制获取锁的等待时间
        synchronized (ReentrantLockDemo.class) {

        }
        ReentrantLock reentrantLock = new ReentrantLock();
        try {
            // 可以通过使用tryLock控制获取锁的等待时间
            // 对于tryLock方法只有在获取锁成功后才会返回true,获取锁失败则会返回false
            if (reentrantLock.tryLock(10, TimeUnit.SECONDS)) {

            }
        } catch (InterruptedException e) {
            // 由于在等待获取锁的过程中,线程有可能已经中止,因此会抛出线程中止异常
            e.printStackTrace();
            // 由于在AQS判断中当前线程已被中止,但其结果存在一些运算操作因此为了保证当前线程不会再继续执行,则手动终止当前线程
            Thread.currentThread().interrupt();
        }
    }
}
