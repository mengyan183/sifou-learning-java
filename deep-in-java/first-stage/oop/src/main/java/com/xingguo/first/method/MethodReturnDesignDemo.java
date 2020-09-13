/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * MethodReturnDesignDemo
 * 方法返回值相关设计
 *
 * @author guoxing
 * @date 9/13/20 9:26 PM
 * @since
 */
public class MethodReturnDesignDemo {
    public static void main(String[] args) {
        List<Integer> integers = listErrorReadList();
        // 该操作会抛出异常
//        integers.add(1);
        // 允许修改操作, 所以返回的数据不是一个完全只读集合
        integers.set(0, 0);
        // 返回数组
        Integer[] integerArray = listIntegerArray();
        // 只允许修改操作,操作的局限性存在
        integerArray[0] = 0;
        // 返回一个完全只读的集合
        List<Integer> unmodifiableList = listUnmodifiableList();
        // 该操作会抛出异常
//        unmodifiableList.add(1);
        // 该操作也会抛出异常
//        unmodifiableList.set(0,0);
        // 获取快照数据
        List<Integer> snapshotList = listSnapshotList();
        snapshotList.add(1);
        snapshotList.set(1, 1);
        System.out.println(snapshotList);
        // 修改操作并不会影响原数据
        System.out.println(listSnapshotList());
    }

    // 方法返回值(多态/封装)
    // 原则一:返回类型需要抽象(强类型,非Object);抽象返回类型的含义,则调用方越容易处理;返回类型越具体,越难通用
    // 如果返回的是一个集合, Collection 优于List或Set
    // 如果返回的数据不考虑写操作,Iterable 优于 Collection

    // 原则二:尽可能返回JAVA集合框架内的接口,尽量避免数组;相对于数组而言,集合中的接口提供了更多的操作; 而且集合支持返回限制操作(读取/写入)类型接口

    // 对于当前返回的集合,虽然限制了新增操作,但并未限制修改操作
    public static List<Integer> listErrorReadList() {
        return Arrays.asList(1, 2, 3, 4);
    }

    // 返回数组, 虽然保证的长度不可变,但并未限制修改操作
    public static Integer[] listIntegerArray() {
        return new Integer[]{1, 2, 3, 4};
    }

    // 原则三:确保返回的集合只读
    public static List<Integer> listUnmodifiableList() {
//        return Collections.unmodifiableList(Arrays.asList(1,2,3,4));
        return List.of(1, 2, 3, 4);
    }

    // 原则四:如果是非只读集合返回,确保返回的是快照数据
    public static List<Integer> listSnapshotList() {
        // 为了保证数据复制的高效性,尽量使用ArrayList,可以提前申请好空间
        return new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    }


}
