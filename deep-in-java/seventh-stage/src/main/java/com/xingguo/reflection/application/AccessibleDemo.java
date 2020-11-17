/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.reflection.application;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AccessibleDemo
 * 访问权限
 *
 * @author guoxing
 * @date 2020/11/16 8:57 PM
 * @see AccessibleObject
 * @since
 */
@Slf4j
public class AccessibleDemo {

    /**
     * 访问性检查所带来的性能损耗
     * 当调用 setAccessible
     *  值为 true 时:表示在使用时会取消访问性检查控制
     *  置为false 时:表示在使用时必须强制进行访问性检查,因此带来的性能损耗
     *
     * @param args
     */
    public static void main(String[] args) throws NoSuchMethodException {
        String a = "hello";
        Class<? extends String> aClass = a.getClass();
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(
                () -> {
                    try {
                        Method toString = aClass.getMethod("toString");
                        // 默认情况下为false,在使用时强制进行安全性检查
                        executeWithTimeStamp(() -> {
                            try {
                                toString.invoke(a);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }, 1000000, null);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
        );

        executorService.execute(
                () -> {
                    try {
                        Method toString = aClass.getMethod("toString");
                        toString.setAccessible(false);
                        executeWithTimeStamp(() -> {
                            try {
                                toString.invoke(a);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }, 1000000, false);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
        );

        executorService.execute(
                () -> {
                    try {
                        Method toString = aClass.getMethod("toString");
                        toString.setAccessible(true);
                        executeWithTimeStamp(() -> {
                            try {
                                toString.invoke(a);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }, 1000000, true);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
        );


        // 访问性检查 : Accessible#isAccessible

        // 修改访问性 : Accessible#setAccessible


        executorService.shutdown();
    }

    public static void executeWithTimeStamp(Runnable runnable, long times, Boolean b) {
        long l = System.currentTimeMillis();
        for (long i = 0; i < times; i++) {
            runnable.run();
        }
        log.info("{}执行时间{}ms", b, System.currentTimeMillis() - l);
    }


}
