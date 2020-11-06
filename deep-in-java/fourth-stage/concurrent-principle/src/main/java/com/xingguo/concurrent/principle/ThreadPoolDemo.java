/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.principle;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolDemo
 * 线程池学习
 *
 * @author guoxing
 * @date 2020/11/2 8:16 PM
 * @since
 */
@Slf4j
public class ThreadPoolDemo {

    public static void main(String[] args) {
        /**
         * 对于线程池从数据结构层面理解, 实际数组搭配队列的形式
         * 对于 数组 主要是针对 核心线程和最大线程数量
         * 而队列主要是指代等待执行的任务
         *
         * 对于线程池的的工作流程是通过提交 worker的形式,而对于 worker 实际是 Runnable的实现类,以及继承了AQS,
         * 对于继承AQS主要是为了实现简单锁;而对于实现Runnable主要是为了通过Thread#start回调当前当前重写的Runnable#run方法来执行真正的任务;
         * 对于每个worker而言实际就是起到类似调度器的作用, 当每个worker执行完成一个run方法时,会继续从等待队列中获取等待的任务; 对于不同任务使用的相同的JavaThread对象执行;
         * 因此如果使用ThreadLocal就有可能存在脏数据存在,因此对于 ThreadLocal只能存储一些无状态的数据
         */
        ExecutorService executorService = new ThreadPoolExecutor(1, 3, 1L, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy());
        executorService.execute(() -> {
            log.info("i love Java");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.execute(() -> {
            log.info("i love Java");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
    }

}
