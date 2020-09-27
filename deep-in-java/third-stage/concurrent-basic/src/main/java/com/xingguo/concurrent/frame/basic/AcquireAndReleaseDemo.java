/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

/**
 * AcquireAndReleaseDemo
 * 锁获取和释放
 * TODO : 尚未验证
 *
 * @author guoxing
 * @date 2020/9/26 9:32 AM
 * @since
 */
public class AcquireAndReleaseDemo {
    public static void main(String[] args) {
        //锁的机制
        // acquire 获取
        // 线程进入同步代码块执行加锁
        // release 释放
        //1:当线程持有锁时,调用Object#wait
        //2:Thread#park 实际调用的是 LockSupport#park ,线程挂起 ?????????
        //3:Condition#await
        // 4:当前持有锁的线程消亡
        //5:java9 自旋锁 Thread.onSpinWait
        //6:Thread.yield


        //锁的公平和非公平
        // 公平(fair) 表示的是在等待队列中的线程 FIFO
        //不公平(unfair) 等待线程随机调度
        // 在创建线程时尽量不要指定线程的优先级
    }
}
