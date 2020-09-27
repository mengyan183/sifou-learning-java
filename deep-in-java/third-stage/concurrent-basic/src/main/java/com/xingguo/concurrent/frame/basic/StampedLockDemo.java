/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLockDemo
 *
 * @author guoxing
 * @date 2020/9/27 2:11 PM
 * @since
 */
@Slf4j
public class StampedLockDemo {
    public static void main(String[] args) {
        StampedLock stampedLock = new StampedLock();
        long writeLockStamp = stampedLock.writeLock();
//        long l = stampedLock.tryOptimisticRead();
        // 转换为读锁,对于写锁本身时互斥锁,并不能自动降级,因此可以使用api主动转换锁类型
        long l = stampedLock.tryConvertToOptimisticRead(writeLockStamp);
        boolean validate = stampedLock.validate(l);
        log.info("是否为读锁:{}", StampedLock.isReadLockStamp(l));
        if (validate) {
            // 当锁进行人工转换时,则原来的锁已经不存在
            log.info("获取到优势读锁");
        } else {
            log.info("未获取到优势读锁");
            stampedLock.unlockWrite(writeLockStamp);
        }

        long readLockStamp = stampedLock.readLock();
        if (stampedLock.validate(readLockStamp)) {
            log.info("执行读锁加锁成功");
            l = stampedLock.tryOptimisticRead();
            if (stampedLock.validate(l)) {
                // 对于优势读锁不需要主动释放锁
                log.info("转换为优势读锁成功");
//                long writeLock = stampedLock.writeLock(); // 当前代码会被阻塞
                stampedLock.tryConvertToReadLock(l);
                log.info("写锁加锁成功");
            }
            stampedLock.unlockRead(readLockStamp);
        }
    }
}
