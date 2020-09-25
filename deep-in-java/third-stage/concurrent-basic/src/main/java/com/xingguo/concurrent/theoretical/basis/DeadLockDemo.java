/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.theoretical.basis;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * DeadLockDemo
 * 死锁表示 竞争双方互相持有对方所需要的共享数据,相互等待对方释放
 *
 * @author guoxing
 * @date 2020/9/25 2:55 PM
 * @since
 */
@Slf4j
public class DeadLockDemo {
    public static void main(String[] args) {

    }


    public static void syncDeadLock(){
        Object l1 = new Object();
        Object l2 = new Object();
        new Thread(() -> {
            synchronized (l1) {
                log.info("当前线程:{}持l1共享变量", Thread.currentThread().getId());
                try {
                    log.info("当前线程:{}准备休眠释放l1共享变量", Thread.currentThread().getId());
                    /**
                     * 在其注解中可以看到
                     * The thread does not lose ownership of any monitors.
                     * 对于sync 实际是利用的monitor ,因此当当前线程休眠时,并不会释放l1的所有权,因此另外一个线程还是被阻塞
                     */
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //TODO : 当前线程并没有释放,因此就没有重新获取一说
                log.info("当前线程:{}休眠结束重新持有l1共享变量", Thread.currentThread().getId());
                synchronized (l2){
                    log.info("当前线程:{}持l2共享变量", Thread.currentThread().getId());
                }
                log.info("当前线程:{}释放l2共享变量", Thread.currentThread().getId());
            }
            log.info("当前线程:{}释放l1共享变量", Thread.currentThread().getId());
        }).start();

        new Thread(() -> {
            synchronized (l2) {
                log.info("当前线程:{}持l2共享变量", Thread.currentThread().getId());
                synchronized (l1){
                    log.info("当前线程:{}持l1共享变量", Thread.currentThread().getId());
                }
                log.info("当前线程:{}释放l1共享变量", Thread.currentThread().getId());
            }
            log.info("当前线程:{}释放l2共享变量", Thread.currentThread().getId());
        }).start();
        /**
         * 对于第一个线程首先持有了 l1 并未释放,去竞争l2发现第二个线程正在持有l2,此时当前线程就需要等待另外的线程释放l2
         * 第二个线程首先持有了 l2 并未释放,去竞争l1发现第一个线程正在持有l1,需要等待另外的线程释放l1
         * 在这种情况下,由于双方互相等待对方释放竞争资源,导致双方都获取不到资源
         */
    }
}
