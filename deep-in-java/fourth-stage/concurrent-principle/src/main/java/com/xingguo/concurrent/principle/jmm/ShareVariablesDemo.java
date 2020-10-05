/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.principle.jmm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ShareVariablesDemo
 *
 * @author guoxing
 * @date 2020/10/4 4:12 PM
 * @since
 */
public class ShareVariablesDemo {
    public static void main(String[] args) {
        // 对于当前对象而言实际 是使用的 value 字段来存储当前值
        AtomicInteger atomicInteger = new AtomicInteger(1);
        // 对于字段 VALUE 实际就是获取的当前AtomicInteger对象实例中字段value的偏移量
        // 对于当前 incrementAndGet 方法而言实际就是通过 当前 atomicInteger的地址值(绝对地址)以及 VALUE(value的相对地址) 来操作当前对象中的属性
        atomicInteger.incrementAndGet();
    }
}
