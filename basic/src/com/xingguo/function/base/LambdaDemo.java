/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.function.base;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * LambdaDemo
 * 函数式编程 lambda
 *
 * @author guoxing
 * @date 9/10/20 2:56 PM
 * @since
 */
public class LambdaDemo {
    public static void main(String[] args) {
        // 详细的流程体现了不同的流程的功能
        Stream.of(1, 2, 3, 4, 5)
                .map(Integer::longValue)
                .distinct()
                .collect(Collectors.toList());
        // 直接使用lambda表达式
        // 当接口中存在多个非default方法时,会直接抛出编译错误 : Multiple non-overriding abstract methods found in interface com.xingguo.function.base.LambdaDemo.Action
        Action action = (s) -> {
            return "1".compareTo(s);
        };
        Integer integer = action.compareTo("1");

    }
    // 对于 SCFP + ACTion

    //supplier 无参有返回值
    public static void supply() {
        Supplier<String> s = () -> "helloworld";
        s = () -> {
            return "helloworld";
        };
    }

    // consumer 有参无返回值
    public static void consumer() {
        // 基本lambda版本
        PropertyChangeListener listener = evt -> {
            System.out.println(evt);
        };
        // lambda简化版本, 通过后面的lambda表达式体现了 code as data
        PropertyChangeListener listener1 = System.out::println;
    }

    // function 有参有返回值
    public static void function() {
        // 对于当前Function的泛型第一个泛型为入参,第二个参数为返回值
        // 只能有一个入参才可以直接使用lambda function表达式
        Function<String, Integer> f = LambdaDemo::compareTo;
        Integer hello = f.apply("hello");
    }

    static int compareTo(String v) {
        return v.compareTo("hello");
    }
//    @FunctionalInterface // 非必须
    interface Action {
        Integer compareTo(String s);
// 如果直接使用lambda表达式或使用注解,要求只能存在一个非default的方法
//        String covertToInteger(Integer integer);

        default void print(String s) {
            System.out.println(s);
        }
    }

}
