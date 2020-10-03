/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.principle;

import lombok.extern.slf4j.Slf4j;

/**
 * SimilarPthreadDemo
 *
 * @author guoxing
 * @date 2020/10/3 10:43 AM
 * @since
 */
@Slf4j
public class SimilarPthreadDemo {
    public static void main(String[] args) throws InterruptedException {
        // 创建线程对象
        Thread thread = new Thread(SimilarPthreadDemo::helloWorld);
        // 创建和os映射的线程
        thread.start();
        // 等待线程执行结束
        thread.join();
    }

    static void helloWorld() {
        log.info("线程id:{};helloWorld", Thread.currentThread().getId());
    }
}
