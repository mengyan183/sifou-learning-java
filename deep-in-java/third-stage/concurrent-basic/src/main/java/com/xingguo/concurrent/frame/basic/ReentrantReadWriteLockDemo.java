/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLockDemo
 *
 * @author guoxing
 * @date 2020/9/27 1:52 PM
 * @since
 */
public class ReentrantReadWriteLockDemo {

    public static void main(String[] args) {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        if (readLock.tryLock()) {
            readLock.unlock();
            try {
                writeLock.lock(); // 在读锁未释放情况下执行写锁加锁,会被一直阻塞,不会往下执行
                {
                    //执行相关写的操作
                }
                // 当写锁加锁成功后可以降级为读锁
                readLock.lock();
            } finally {
                // 释放写锁
                writeLock.unlock();
            }
        }
        {
            // 执行读操作
            try {

            } finally {
                // 保证所有的加锁和解锁都是成对存在
                // 如果读锁不释放,可能导致的结果为所有的读操作都不会有影响,但写操作会被一直阻塞
                readLock.unlock();
            }
        }

    }
}
