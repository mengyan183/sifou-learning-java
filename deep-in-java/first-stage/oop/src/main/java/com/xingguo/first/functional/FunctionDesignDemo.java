/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional;

import java.util.function.Function;

/**
 * FunctionDesignDemo
 * Function 设计
 *
 * @author guoxing
 * @date 9/14/20 3:41 PM
 * @since
 */
public class FunctionDesignDemo {

    public static void main(String[] args) {
        //
        Function function = a -> a;
        // 实际对于Function 实际是将一段代码封装为一个函数,作为延时调用
        Function<Integer, Integer> integerIntegerFunction = a -> a / 2;
        Integer apply = integerIntegerFunction.apply(1);
        System.out.println(apply);
    }
}
