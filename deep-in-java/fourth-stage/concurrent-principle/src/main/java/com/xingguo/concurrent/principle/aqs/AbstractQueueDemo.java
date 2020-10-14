/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.principle.aqs;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AbstractQueueDemo
 *
 * @author guoxing
 * @date 2020/10/14 11:44 AM
 * @since
 */
@Slf4j
public class AbstractQueueDemo {
    public static void main(String[] args) {
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//        executorService.submit(AbstractQueueDemo::action);
//        executorService.submit(AbstractQueueDemo::action);
//        executorService.shutdown();
        conditionObjectDemo();
    }

    public static final Lock LOCK = new ReentrantLock();

    public static void action() {
        String name = Thread.currentThread().getName();
        try {
            /**
             * 当并发线程执行时大概的时序图
             * 存在两个线程  pool-1-thread-1 和 pool-1-thread-2
             * 当第一个线程 pool-1-thread-1 获得到锁时, 由于System.in.read() 的原因导致当线程被阻塞等待控制台输入;
             * 与此同时,第二个线程 pool-1-thread-2 需要等待 pool-1-thread-1 释放当前持有的锁资源,因此会调用java.util.concurrent.locks.AbstractQueuedSynchronizer#acquire(java.util.concurrent.locks.AbstractQueuedSynchronizer.Node, int, boolean, boolean, boolean, long)操作,
             *  第一次循环:由于当前操作是第一次执行,会首先为当前线程创建一个排他节点(node0)
             *  继续下一次循环,设置当前新创建的节点等待线程为当前线程,由于当前AQS双向链表尚未创建,此时会使用cas操作生成一个新的head节点(node1),并将tail节点指向当前新的node,对于当前操作的结果就是生成了一个新的节点,此时head和tail都指向同一个对象表示当前链表初始化生成结束; head <=> tail -> node1
             *  继续下一次循环,由于此时当前head和tail 都是指向的相同的无意义节点,设置node0 的 prev 指向 node1,会执行cas操作设置当前aqs队列的tail指向node0,并设置node1 的 next 指向 node0,此时aqs的结构就变成了 head(node1) <-> tail(node0)
             *  继续下一次循环,由于当前自旋次数(spins是为了满足超时等待情况下的操作)不为0,当前node0 中的 status 为默认值 0,修改 node0 的status 为 1
             *  继续下一次循环,此时就会进入最后一个else判断,首先会变更当前spins 的值为 1,默认是非超时等待此时会挂起当前线程(pool-1-thread-2),代码被阻塞到此处
             *
             *  当线程pool-1-thread-1 释放锁时,会通知到当前被挂起的线程 pool-1-thread-2,此时pool-1-thread-2会从继续从上一次阻塞处的代码继续往后执行,修改当前node0的status 为 0,
             *  继续下一次循环,此时再去执行tryacquire 就会获取锁成功,如果当前节点为第一个节点(first == true),因此会修改当前节点作为head节点,并结束当前acquire方法
             */
            log.info("{}线程准备获取锁", name);
            LOCK.lock();
            log.info("{}线程成功获取锁,并等待控制台输入", name);
            System.in.read();// 阻塞当前线程等待控制台输入
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /**
             * 在当前线程释放锁时,首先会修改当前aqs 的 state 的状态, 并会调用 java.util.concurrent.locks.AbstractQueuedSynchronizer#signalNext(java.util.concurrent.locks.AbstractQueuedSynchronizer.Node)来通过 head 节点获取head.next(node0), 如果node0 的status 不为0,则修改node0 的status 为 1, 并唤醒当前node0 的waiter(线程),使被唤醒的线程继续竞争锁
             */
            LOCK.unlock();
            log.info("{}线程释放锁", name);
        }
    }

    public static void conditionObjectDemo(){
        Condition condition = LOCK.newCondition();
        new Thread(()->{
            try {
                LOCK.lock();
                log.info("当前线程{}准备进入阻塞",Thread.currentThread().getName());
                condition.await(); // 通过 conditionNode 的 status 来实现线程通信以及线程挂起设置,且会release 当前线程持有的锁
                log.info("当前线程{}被唤醒",Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                LOCK.unlock();
            }
        }).start();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOCK.lock();
        condition.signal();// 将当前condition 持有的 conditionNode 追加到当前AQS中
        LOCK.unlock(); // 释放当前线程持有的锁,并唤醒当前aqs中的下一个node
    }
}
