/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.basic;

/**
 * DumpThreadDemo
 *
 * @author guoxing
 * @date 2020/9/23 1:12 PM
 * @since
 */
public class DumpThreadDemo {
    // 打印堆栈信息
    public static void main(String[] args) {
        // 底层实际也是通过异常的方式来输出堆栈信息
        Thread.dumpStack();
        // 通过自定义异常来输出堆栈信息
        new Throwable("Stack trace").printStackTrace(System.out);
        // 对于上面两行代码底层都是使用的StackTraceElement 获取当前线程完整的堆栈信息,相对来说比较重
        // 对于StackWalker是一个相对轻量级的获取当前堆栈信息
        StackWalker instance = StackWalker.getInstance();
        instance.forEach(System.out::println);
    }
}
