/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.basic;

/**
 * ThreadDemo
 *
 * @author guoxing
 * @date 2020/9/23 9:18 AM
 * @since
 */
public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        // 创建新的线程,但此时并未创建一个真正的线程
        Thread thread = new Thread(() -> {
            Thread currentThread = Thread.currentThread();
            System.out.println("当前线程id:" + currentThread.getId());
            System.out.println(currentThread.getState());
        });
        System.out.println(thread.getState());
        // 只启动线程,异步线程并不一定会执行,并不会等待线程真正结束
        // 在调用start0时才会创建新的线程
        thread.start();
        System.out.println(thread.getState());
        // 根据异步线程可以看出只有当主线程挂起时,才会开始执行异步线程
        System.out.println("1");
        Thread.sleep(1000);
        System.out.println(2);
        // 等待异步线程执行结束
        thread.join(); // 当调用join方法时,会将当前异步线程变为同步线程,必须等待当前线程执行结束后,主线程才会继续执行
        System.out.println(thread.getState());
    }
}
