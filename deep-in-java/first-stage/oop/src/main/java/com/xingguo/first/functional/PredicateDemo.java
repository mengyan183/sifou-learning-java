/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional;

import java.util.function.Predicate;

/**
 * PredicateDemo
 * 判断类型: 有入参,返回值永远为布尔
 *
 * @author guoxing
 * @date 9/14/20 11:34 AM
 * @since
 */
public class PredicateDemo {

    public static void main(String[] args) {
        // 判断类型,用于判断当前数据输入参数是否满足条件,结果只有true or false
        // 例如Stream.filter实际就是利用的Predicate
//        Predicate<String> predicate = "1"::equals;
        Predicate<String> predicate = new Predicate<>() {
            @Override
            public boolean test(String s) {
                return "1".equals(s);
            }
        };
        boolean test = predicate.test("1");
        System.out.println(test);
        // 翻转判断
        System.out.println(predicate.negate().test("1"));
        // 同样支持链式编程,通过 && 连接多个判断
        boolean test1 = predicate.and("2"::equals).test("1");
        System.out.println(test1);
        // 通过 || 或来连接多个判断
        boolean test2 = predicate.or("2"::equals).test("1");
        System.out.println(test2);
    }
}
