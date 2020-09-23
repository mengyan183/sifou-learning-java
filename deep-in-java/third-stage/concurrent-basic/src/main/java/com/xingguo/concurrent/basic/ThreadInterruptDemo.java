/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.basic;

/**
 * ThreadInterruptDemo
 * 线程中止
 *
 * @author guoxing
 * @date 2020/9/23 2:44 PM
 * @since
 */
public class ThreadInterruptDemo {

    public static void main(String[] args) throws InterruptedException {
        // 对于线程Interrupt实际是对当前线程中的interrupt属性进行变更,并非真正的中止方法
        // 创建线程对象
        Thread thread = new Thread(() -> {
            boolean interrupted = printCurrentThreadInterrupt();
            boolean b = blockInterruptThread();
            if (!b) {
                return;
            }
            if (interrupted) {
                System.out.println("当前线程已被中止");
                return;
            }
            System.out.println("hello");
        });
        // 变更线程对象中止状态
        // 在调用中止方法时,如果调用方线程和要操作的线程不相同,则会加上同步锁后再执行操作
        thread.interrupt();
        // 启动当前线程,并创建一个真正的和系统线程 1:1 的线程
        thread.start();
        // 将当前异步线程阻塞变更为同步状态
        thread.join();
    }

    public static boolean printCurrentThreadInterrupt() {
        Thread currentThread = Thread.currentThread();
        boolean interrupted = currentThread.isInterrupted();
        System.out.println("当前线程interrupted状态:" + currentThread.isInterrupted());
        return interrupted;
    }

    public static boolean blockInterruptThread() {
        Object monitor = new Object();
        synchronized (monitor) {
            try {
                // 如果当前线程的interrupt状态为true,阻塞当前线程,则会抛出异常:java.lang.InterruptedException
                monitor.wait(); // 如果不单独持有当前对象,当调用wait方法时会抛出java.lang.IllegalMonitorStateException: current thread is not owner
            } catch (InterruptedException e) {
                e.printStackTrace();
                // 当抛出InterruptedException异常后,线程的interrupt状态会变更为false
                printCurrentThreadInterrupt();
                return false;
            }
        }
        return true;
    }
}
