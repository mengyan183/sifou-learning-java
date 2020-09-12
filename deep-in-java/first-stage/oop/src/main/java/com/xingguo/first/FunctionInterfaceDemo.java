/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first;

import java.lang.annotation.Annotation;

/**
 * FunctionInterfaceDemo
 *
 * @author guoxing
 * @date 9/11/20 1:04 PM
 * @since
 */
public class FunctionInterfaceDemo {

    @FunctionalInterface
    interface FunctionInterfaceTest {
        int get();
    }

    public static void main(String[] args) {
        // 直接使用lambda表达式
        FunctionInterfaceTest functionInterfaceTest = () -> 1;
        int i = functionInterfaceTest.get();
    }

}
