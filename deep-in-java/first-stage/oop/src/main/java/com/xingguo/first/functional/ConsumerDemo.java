/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional;

import java.util.function.Consumer;

/**
 * ConsumerDemo
 * 消费者模式 - 有入参,无返回值
 *
 * @author guoxing
 * @date 9/14/20 10:49 AM
 * @since
 */
public class ConsumerDemo {

    public static void main(String[] args) {
        // 创建两个consumer
        Consumer<String> consumer = System.out::println;
        Consumer<String> consumer1 = ConsumerDemo::printLn;
        // consumer通过andThen支持链式编程
        consumer.andThen(consumer1).accept("输出");
    }

    // 只有入参无返回值
    public static void printLn(String s) {
        System.out.println("printLn:" + s);
    }
}
