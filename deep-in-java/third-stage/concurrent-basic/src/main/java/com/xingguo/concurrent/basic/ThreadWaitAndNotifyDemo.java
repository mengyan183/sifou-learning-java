/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.basic;

/**
 * ThreadWaitAndNotifyDemo
 * 多线程之间通讯
 * 利用Object的monitor来实现线程的通讯,这里的通讯仅指代唤醒操作
 *
 * @author guoxing
 * @date 2020/9/23 4:32 PM
 * @since
 */
public class ThreadWaitAndNotifyDemo {

    public final static Object MONITOR = new Object();

    public static void main(String[] args) {

        for (int i = 0; i < 2; i++) {
            new Thread(ThreadWaitAndNotifyDemo::sayHello, "T" + i).start();
        }
        synchronized (MONITOR){
            System.out.println("主线程执行唤醒");
            // 通过查看C++源码简单分析 ObjectMonitor::notify
            //当存在多个线程同时阻塞时,在唤醒时,如果接到通知的线程和等待队列中队头不一致时,会导致没有任何一个线程被唤醒
//            MONITOR.notify();
            // 而对于notifyAll是不断迭代所有等待的线程
            MONITOR.notifyAll();
        }
        System.out.println("主线程阻塞");
    }

    public static void sayHello() {
        Thread thread = Thread.currentThread();
        synchronized (MONITOR) {
            try {
                System.out.println("当前线程" + thread.getName() + "进入等待状态");
                MONITOR.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("当前线程" + thread.getName() + "退出等待状态");
            System.out.println("helloWorld;");
        }
    }
}
