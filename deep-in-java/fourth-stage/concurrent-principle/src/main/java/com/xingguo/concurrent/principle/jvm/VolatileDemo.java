/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.principle.jvm;

/**
 * VolatileDemo
 *
 * @author guoxing
 * @date 2020/10/16 11:39 AM
 * @since
 */
public class VolatileDemo {
    public static void main(String[] args) {
        // 对于 volatile 实际是利用了 c++ 中的 orderAccess 中的内存屏障实现了可见性
    }
}
