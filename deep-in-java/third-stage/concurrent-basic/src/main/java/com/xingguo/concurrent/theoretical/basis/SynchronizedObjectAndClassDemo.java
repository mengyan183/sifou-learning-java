/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.theoretical.basis;

/**
 * SynchronizedObjectAndClassDemo
 * 锁定的目标 对象和类的区别
 *
 * @author guoxing
 * @date 2020/9/24 1:40 PM
 * @since
 */
public class SynchronizedObjectAndClassDemo {

    public final static Object MONITOR_A = new Object();
    public final static Object MONITOR_B = new Object();

    public static void lockA() {
        synchronized (MONITOR_A) {
            System.out.println("lockA");
            lockB();
        }
    }

    public static void lockB() {
        synchronized (MONITOR_B) {
            System.out.println("lockB");
        }
    }


    // 对于锁定 对象和类 的区别在于,其锁定的范围不同,对于锁定对象实际临界条件为当前对象,而当锁定类时,实际锁定的当前类
    // 对于 .class 实际也是一个Object 对象,因此实际并没有太大的区别
    public static void main(String[] args) {
        // 对象锁
        synchronized (new Object()) {
            // 对象存在于堆中

        }
        // 类锁
        synchronized (SynchronizedObjectAndClassDemo.class) {
            // 类存在于 metadata(java8+);
            //类存在于 perm(java7-)
        }
        Object o = SynchronizedObjectAndClassDemo.class;
        echo("hello");// 当前重进入次数为 三次 ; echo -> println -> print(x);或newLine();
        synchronized (MONITOR_B) { // 对于lockB而言的 MONITOR_B 实际就属于重进入,原因在于,在最外层以及得到了MONITOR_B 竞态条件的所有权,当在内部再次 使用 synchronized (MONITOR_B),由于线程在外部已经得到了竞态条件所有权,因此在内部再次使用时并不需要对竞态条件再次进行判断,直接进入;因此这种操作就属于重进入
            lockA();
        }

    }

    // 锁定的静态条件是相同的
//    public synchronized static void echo(String message){
    public static void echo(String message) {
        synchronized (SynchronizedObjectAndClassDemo.class) {
            System.out.println(message);
        }
    }

    // 锁定的对象是相同的
    public synchronized void echoNoStatic(String message) {
    }// 方法
//    public void echoNoStatic(String message) {
//        // 代码块
//        synchronized (this) { // 对于当前的竞态条件为当前调用实例
//
//        }
//    }

}
