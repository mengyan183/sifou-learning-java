/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.principle;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * PthreadMutexDemo
 * 互斥对象
 *
 * @author guoxing
 * @date 2020/10/3 9:11 PM
 * @since
 */
public class PthreadMutexDemo {

    public static void main(String[] args) {
        // 由于是静态变量的原因,数据是存储在meta-data中存储进行数据共享,生命周期是和class绑定到一起的
        // 当不加锁时,before 看到的都是0
//        new StaticShareData().concurrentFreeLock();
        // 当加锁操作时,before看到的则不同
//        new StaticShareData().concurrentWithLock();

//        new ShareData().concurrentFreeLock();
//        new ShareData().concurrentWithLock();

//        new LocalData().concurrentFreeLock();
//        new LocalData().concurrentWithLock();
//        new LocalData().testValue();
        new LocalData().testAtomic();
    }
}

@Slf4j
class StaticShareData {
    private static final Lock LOCK = new ReentrantLock();
    private static int count;

    public void concurrentFreeLock() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(this::addCount);
        executorService.submit(this::addCount);
        executorService.shutdown();
    }

    public void concurrentWithLock() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            LOCK.lock();
            addCount();
            LOCK.unlock();
        });
        executorService.submit(() -> {
            LOCK.lock();
            addCount();
            LOCK.unlock();
        });
        executorService.shutdown();
    }

    private void addCount() {
        log.info("currentThread:{};before count :{}", Thread.currentThread().getName(), count);
        count++;
        log.info("currentThread:{};after count :{}", Thread.currentThread().getName(), count);
    }
}

@Slf4j
class ShareData {
    private static final Lock LOCK = new ReentrantLock();
    private int count;

    public void concurrentFreeLock() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(this::addCount);
        executorService.submit(this::addCount);
        executorService.shutdown();
    }

    public void concurrentWithLock() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            LOCK.lock();
            addCount();
            LOCK.unlock();
        });
        executorService.submit(() -> {
            LOCK.lock();
            addCount();
            LOCK.unlock();
        });
        executorService.shutdown();
    }

    private void addCount() {
        log.info("currentThread:{};before count :{}", Thread.currentThread().getName(), count);
        count++;
        log.info("currentThread:{};after count :{}", Thread.currentThread().getName(), count);
    }
}

/**
 * 对于局部变量 数字类型数据 , 无论包装类型还是基本类型,当发生数据传递后
 */
@Slf4j
class LocalData {
    private static final Lock LOCK = new ReentrantLock();

    public void concurrentFreeLock() {
        Integer count = 0;
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            addCount(count);
        });
        executorService.submit(() -> addCount(count));
        executorService.shutdown();
        log.info("{}", count);
    }

    public void concurrentWithLock() {
        Integer count = 0;
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            LOCK.lock();
            addCount(count);
            LOCK.unlock();
        });
        executorService.submit(() -> {
            LOCK.lock();
            addCount(count);
            LOCK.unlock();
        });
        executorService.shutdown();
        log.info("{}", count);

    }

    public void testValue() {
        // 对于基本数据类型的包装对象,为什么发生数据传递后,数据改变不会影响原有的值?
        // 可以看到根据输出日志,对于count对象而言,在传递后addCount方法后,实际是将方法的参数执行外部的count对象;当发生自增操作之后,对于方法上的参数实际是指向了新的对象,而局部变量count指向的还是原来的对象;原因在于count自增操作并不是操作的同一个对象
        Integer count = 0;
        log.info("数据传递前内存地址:{}",System.identityHashCode(count));
        addCount(count);
        log.info("数据传递后内存地址:{}",System.identityHashCode(count));
        log.info("{}", count);
    }

    public void testAtomic(){
        AtomicInteger atomicInteger = new AtomicInteger();
        log.info("数据传递前内存地址:{}",System.identityHashCode(atomicInteger));
        addAtomicCount(atomicInteger);
        log.info("数据传递后内存地址:{}",System.identityHashCode(atomicInteger));
    }

    private void addAtomicCount(AtomicInteger atomicInteger) {
        log.info("数据传递后自增前内存地址:{}",System.identityHashCode(atomicInteger));
        atomicInteger.incrementAndGet();
        log.info("数据传递后自增后内存地址:{}",System.identityHashCode(atomicInteger));
    }

    private void addCount(Integer count) {
        log.info("currentThread:{};before count :{}", Thread.currentThread().getName(), count);
        log.info("数据传递后自增前内存地址:{}",System.identityHashCode(count));
        //count = Integer.valueOf(count.intValue()+1);
        count++;
        log.info("数据传递后自增后内存地址:{}",System.identityHashCode(count));
        log.info("currentThread:{};after count :{}", Thread.currentThread().getName(), count);
    }
}
