/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.principle.jvm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicDemo
 *
 * @author guoxing
 * @date 2020/10/16 11:39 AM
 * @since
 */
public class AtomicDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.incrementAndGet(); // 这里实际是使用了汇编指令实现原子操作
    }
}
