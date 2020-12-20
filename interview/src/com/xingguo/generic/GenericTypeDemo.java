/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.generic;

import java.util.ArrayList;

/**
 * GenericTypeDemo
 *
 * @author guoxing
 * @date 2020/12/20 2:49 PM
 * @since
 */
public class GenericTypeDemo {
    public static void main(String[] args) {
        ArrayList<Fruit> fruits = new ArrayList<>();
        // 当前情况成功
        fruits.add(new Apple());

        ArrayList<Apple> apples = new ArrayList<>();
        apples.add(new Apple());
        fruits.addAll(apples);
        fruits.forEach(System.out::println);

        //由于编译时的语法限制,当前代码会抛出错误
//        fruits.addAll(new ArrayList<Object>());
    }
}
