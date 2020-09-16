/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection;

import java.util.*;

/**
 * ListAndSetDemo
 *
 * @author guoxing
 * @date 9/15/20 5:48 PM
 * @since
 */
public class ListAndSetDemo {
    public static void main(String[] args) {
        Set<String> strings = new HashSet<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");

        strings.forEach(System.out::println);
        // 对于部分的String类型数据,在使用HashSet时会产生误打误撞的有序数据
        // 原因在于 java.lang.String.hashCode
        /**
         *     int h = 0;
         *         for (byte v : value) {
         *             h = 31 * h + (v & 0xff);
         *         }
         *         return h;
         */
        // 对于hashCode方法中实际是利用的字符串的ascii码来生成hash, 因此对于有序的简单字符得到的hash值也是有序的,因此会导致set中的数据也是有序的
        strings.clear();
        strings.add("a");
        strings.add("b");
        strings.add("c");
        strings.forEach(System.out::println);
    }

    // 对于 NavigableSet 实际支持一致性hash算法,支持负载均衡

}
