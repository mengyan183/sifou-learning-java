/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.basic;

/**
 * ThreadSuspendDemo
 *
 * @author guoxing
 * @date 2020/9/23 2:10 PM
 * @since
 */
public class ThreadSuspendDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = Thread.currentThread();
        // 对于Thread#suspend/resume 的作用范围仅针对指定的线程
        thread.suspend();
        thread.resume();
        // Object#wait/notify
        // 而Object是利用对象头中的monitor来控制当前所有准备操作当前对象的线程
        thread.wait();
        thread.notify();
    }
}
