/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional;

import java.util.function.Supplier;

/**
 * SupplierDesignDemo
 * supplier设计
 *
 * @author guoxing
 * @date 9/14/20 2:27 PM
 * @since
 */
public class SupplierDesignDemo {
    // 参考 ObjectProvider
    public static void main(String[] args) {
        printLnString("message");// 对于当前数据传递实际存在固化
        // 而对于参数类型为Supplier实际是提供多种代码实现方式支持,并不单一
        printLnString(() -> {
            return getStringWithSleep();
        });
        printLnString(SupplierDesignDemo::getStringWithSleep);// 这种方式表现了一种代码即数据的编程范式
        printLnString(supplyStringWithSleep()); // 将返回的supplier作为参数进行传递
        // 对于当前supplier实际是数据的载体,但并未真正执行逻辑
        Supplier<String> stringSupplier = SupplierDesignDemo::getStringWithSleep;
        // 当执行get操作时实际才真正执行 getStringWithSleep方法中的代码
        String s = stringSupplier.get();
        System.out.println(s);
        // 和当前 getStringWithSleep方法直接对比
        String stringWithSleep = getStringWithSleep(); // 数据及时返回
        System.out.println(stringWithSleep);
    }

    private static String getStringWithSleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "message";
    }

    private static Supplier<String> supplyStringWithSleep() {
        return () -> "message";
    }

    // 通过一组 打印方法来对比 supplier的优势
    public static void printLnString(String message) {
        System.out.println(message);
    }

    public static void printLnString(Supplier<String> stringSupplier) {
        System.out.println(stringSupplier.get());
    }

}
