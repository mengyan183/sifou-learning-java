/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * SupplierDemo
 * 提供者模式 : 无入参,有返回值
 *
 * @author guoxing
 * @date 9/14/20 10:44 AM
 * @since
 */
public class SupplierDemo {

    public static void main(String[] args) {
        Supplier<String> string = getString();
        System.out.println(string.get());

        Callable<Long> callable = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return getCurrentTimeMillis().get();
            }
        };
        try {
            System.out.println(callable.call());
        } catch (Exception e) {

        }
    }

    public static Supplier<String> getString() {
        return () -> "getString";
    }

    public static Supplier<Long> getCurrentTimeMillis() {
        return System::currentTimeMillis;
    }
}
