/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.principle.jmm;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.Period;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * SynchronizedRelationDemo
 * 同步关系
 *
 * @author guoxing
 * @date 2020/10/12 9:48 AM
 * @since
 */
@Slf4j
public class SynchronizedRelationDemo {
    public static void main(String[] args) throws InterruptedException {
//        threadStart();
//        objectInit();
        threadInterrupt();
    }

    private static int sharedData;
    private static final Object LOCK = new Object();

    public static void synchronizedChangeData(int data) {
        /**
         *
         * An unlock action on monitor m synchronizes-with all subsequent lock actions on
         * m (where "subsequent" is defined according to the synchronization order).
         * 对于同步关系中这句话的理解为:
         * 存在多个不同的竞争线程来准备持有LOCK; 此时T1获取到了锁, 而其他的线程处于等待LOCK monitor release中;
         * 此时 T1释放 LOCK, 此时其他的线程中只会有一个线程T2获取到锁;
         * 对于T1 unlock(LOCK) -> T2 lock(LOCK) 对于这两个关系实际是同步的; 这里实际并不会去考虑其他线程竞争当前资源的过程,对于上一个线程解锁和下一个线程加锁实际就是同步的
         *
         */
        synchronized (LOCK) {
            sharedData = data;
        }
    }

    /**
     * A write to a volatile variable v (§8.3.1.4) synchronizes-with all subsequent
     * reads of v by any thread (where "subsequent" is defined according to the
     * synchronization order)
     * <p>
     * 对于我个人理解而言,volatile 和  mysql 可重复读是类似的, 对于并发线程中当存在读线程和写线程操作而言,当写线程执行过写操作后,当读线程再次执行读操作时实际是读取到的写线程修改后的数据,而非读线程中之前读操作得到的数据
     */
    private static volatile int volatileSharedData;

    // volatile 写
    public static void volatileChangeData(int data) {
        volatileSharedData = data;
    }

    // volatile 读
    public static int getVolatileSharedData() {
        return volatileSharedData;
    }


    public static void threadStart() throws InterruptedException {
        Thread thread = new Thread(() -> {
            log.info("线程启动后通过回调调用run方法");
        });
        thread.start();
        thread.join();
    }

    @AllArgsConstructor
    @ToString
    public static class Person {
        private final String name;
        private final int age;
        private final Collection<Integer> tags;
    }

    public static void objectInit() throws InterruptedException {
        List<Integer> tags = Arrays.asList(1, 2, 3);
        /**
         * 对于person对象而言肯定是在创建成功后才可以使用,不可能读取到对象属性的中间状态;
         * 对于person对象而言,在读取到时,对象中的属性肯定已经被赋值结束
         * 对于 原生类型,不可变对象类型实际不可能读取到这些数据的不完整状态
         * 而对于一些可变对象(集合等)而言,对于异步线程操作而言实际是有可能读取到数据的中间状态
         */
        Person person = new Person("guoxing", 18, tags);
        Thread thread = new Thread(() -> {
            log.info("{}", person);
        });
        thread.start();
        Thread.sleep(1000);
        // 将 3 -> 1
        tags.set(2, 1);
        // 通过判断线程isAlive(当前线程执行结束后对象的操作系统线程就会被回收掉了,因此对于join方法执行结束则线程肯定已经执行结束)一直调用 thread对象的wait方法一直阻塞当前对象
        thread.join();
        log.info("{}", person);
    }

    public static void threadInterrupt() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            if (Thread.interrupted()) {
                log.info("当前线程状态已被终止,且会清除当前线程的状态并清除相关的终止事件");
            }
        });

        // 终止T2线程
        Thread t2 = new Thread(() -> {
            t1.interrupt();
            log.info("终止t1线程");
        });
        Thread t3 = new Thread(() -> {
            if (t1.isInterrupted()) {
                log.info("t1线程已被终止");
            }
        });
        // 通过volatile来实现可见性
        // 当 t2 -> t1.interrupt -> t3 会读取到t1线程的interrupt状态,由于当t1操作时会修改t1的interrupt值,如果t3放到最后,则不会执行t3中的打印
        t2.start();
        t3.start();
        t1.start();

        Thread.sleep(5000);

    }
}
