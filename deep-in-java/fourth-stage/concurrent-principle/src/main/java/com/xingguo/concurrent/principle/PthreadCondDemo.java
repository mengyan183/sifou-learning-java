/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.principle;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * PthreadCondDemo
 *
 * @author guoxing
 * @date 2020/10/4 3:24 PM
 * @since
 */
public class PthreadCondDemo {
    public static void main(String[] args) throws InterruptedException {
        // 对于条件变量 java 提供了两种方式
        // 1: Object : 分别提供了 wait 和 notify 以及 notifyAll的方法; 对于这两个方法的使用必须要求已经使用synchronized对要操作的对象已经加锁
        synchronized (Object.class){
//            Object.class.wait();
            // java.lang.IllegalMonitorStateException: current thread is not owner
//            PthreadCondDemo.class.wait(); // 会抛出异常,原因在于当前对象的monitor并未被当前线程获取
        }
        // 2: Condition : 分别提供了 await 和 signal 以及 signalAll的方法;对应这些方法的使用同样要求必须已经使用Lock已加锁
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        reentrantLock.lock();
//        condition.await(); // 当不加锁的情况下会抛出异常 ;java.lang.IllegalMonitorStateException
        reentrantLock.unlock();
    }
}
