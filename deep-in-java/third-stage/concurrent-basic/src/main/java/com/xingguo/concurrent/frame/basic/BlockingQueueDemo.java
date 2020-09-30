/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BlockingQueueDemo
 *
 * @author guoxing
 * @date 2020/9/29 6:48 PM
 * @since
 */
public class BlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
//        new ArrayBlockingQueueDemo().addMethod();
//        new ArrayBlockingQueueDemo().offerMethod();
//        new ArrayBlockingQueueDemo().putMethod();
//        new ArrayBlockingQueueDemo().concurrentMethod();
//        new LinkedBlockingQueueDemo().concurrentMethod();
//        new SynchronousQueueDemo().concurrentMethod();
        new SynchronousQueueDemo().exceptionDemo();
        new SynchronousQueueDemo().deadLockDemo();
    }
}

/**
 * synchronousQueue实际实现了类似同步锁的操作
 */
@Slf4j
class SynchronousQueueDemo extends AbstractBlockingQueueDemo {
    public void concurrentMethod() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        BlockingQueue<Integer> integers = new SynchronousQueue<>();
        concurrentOperateQueue(integers, executorService);
    }

    public void deadLockDemo() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        BlockingQueue<Integer> integers = new SynchronousQueue<>();

        executorService.submit(() -> {
            boolean offer = integers.offer(1);
            log.info("{}", offer);
        });
        executorService.submit(() -> {
            try {
                Integer take = integers.take();// 当前线程会被一直阻塞在此
                log.info("{}", take);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
    }

    public void exceptionDemo() {
        try {
            SynchronousQueue<Integer> integers = new SynchronousQueue<>();
            boolean add = integers.add(1);
            log.info("{}", add);
        } catch (IllegalStateException e) {
            log.error("", e);
        }
    }
}


@Slf4j
class LinkedBlockingQueueDemo extends AbstractBlockingQueueDemo {
    public void concurrentMethod() {
        // 对于当构造方法不指定长度时,则为无界队列,当指定容量时则为有界队列
//        BlockingQueue<Integer> integers = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> integers = new LinkedBlockingQueue<>(2);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        concurrentOperateQueue(integers, executorService);
    }
}


/**
 * 由于BlockingQueue存在容量限制,类似于通道的形式,可以实现简单的生产-消费模式;
 * 对于生产而言,当队列满时需要同步阻塞直到存在空闲空间, 因此对于BlockingQueue 一般多用put少用offer,原因在于put会一直等待,除非线程中断;而offer是在有限时间内等待
 */
@Slf4j
class ArrayBlockingQueueDemo extends AbstractBlockingQueueDemo {
    public void addMethod() {
        ArrayBlockingQueue<Integer> integers = new ArrayBlockingQueue<>(3);
        for (int i = 0; i < 4; i++) {
            // 当超过queue容量时,add方法会抛出异常
            integers.add(i);
        }
    }

    public void offerMethod() {
        ArrayBlockingQueue<Integer> integers = new ArrayBlockingQueue<>(3);
        for (int i = 0; i < 4; i++) {
            // 当超过queue容量时,offer方法不会抛出异常,但会返回false
            boolean offer = integers.offer(i);
            log.info("数据写入队列:{}", offer);
        }
    }

    public void putMethod() throws InterruptedException {
        ArrayBlockingQueue<Integer> integers = new ArrayBlockingQueue<>(3);
        for (int i = 0; i < 4; i++) {
            // 当超过queue容量时,put方法不会抛出异常但当前线程会一直阻塞
            integers.put(i);
        }
    }

    public void concurrentMethod() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        BlockingQueue<Integer> integers = new ArrayBlockingQueue<>(2);
        concurrentOperateQueue(integers, executorService);
    }
}

@Slf4j
abstract class AbstractBlockingQueueDemo {
    public void concurrentOperateQueue(BlockingQueue<Integer> integers, ExecutorService executorService) {
        for (AtomicInteger i = new AtomicInteger(); i.get() < 100; i.incrementAndGet()) {
            executorService.submit(() -> {
                try {
                    // 由于原子对象的缘故,因此当线程写入时实际是对象中实时的数据
                    integers.put(i.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            executorService.submit(() -> {
                try {
                    Integer take = integers.take();
                    log.info("{}", take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }


        executorService.shutdown();
    }
}
