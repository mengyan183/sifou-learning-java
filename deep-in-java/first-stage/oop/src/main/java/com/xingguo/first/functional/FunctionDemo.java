/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional;

import java.util.function.Function;

/**
 * FunctionDemo
 * 转换类型
 *
 * @author guoxing
 * @date 9/14/20 11:19 AM
 * @since
 */
public class FunctionDemo {

    public static void main(String[] args) {
        // 将Long类型转换为String
        Function<Long, String> longToString = String::valueOf;
        // 将String类型数据转换为Long
        Function<String, Long> stringToLong = Long::parseLong;
        String apply = longToString.apply(1L);
        System.out.println(apply);
        // 对于 compose和andThen 的操作不同点在于
        // compose是先执行当前入参的Function; 而andThen是后执行当前入参的Function
        // 操作流程为 "1" -> 1L -> "1"
        // longToString.apply(stringToLong.apply("1"));
        String apply1 = longToString.compose(stringToLong).apply("1");
        // 操作流程为 1L -> "1" -> 1L
        // stringToLong.apply(longToString.apply(1L))
        Long apply2 = longToString.andThen(stringToLong).apply(1L);

    }
}
