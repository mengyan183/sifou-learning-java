/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LegacyCollectionDemo
 * 遗留集合实现
 *
 * @author guoxing
 * @date 9/16/20 10:39 AM
 * @since
 */
public class LegacyCollectionDemo {
    public static void main(String[] args) {
        vectorVsList();
        vectorVsStack();
        hashTableVsHashMap();
        enumeration();
        bitSet();
    }

    private static void bitSet() {
        // BitSet 用于操作位运算集合 一般搭配byteBuffer使用
        // BitSet 更加适合存储连续紧邻的数据存储,会更节省空间,否则会按照最大数据来创建空间
        BitSet bitSet = BitSet.valueOf("i love java".getBytes());
        System.out.println(bitSet.length());
        System.out.println(bitSet.size());
        System.out.println(BitSet.valueOf(new byte[]{1,2,3,4}).length());
        System.out.println(BitSet.valueOf(new byte[]{1,2,3,4}).size());
        System.out.println(BitSet.valueOf(new byte[]{4}).size());


    }

    private static void enumeration() {
        // 对字符串进行分割
        StringTokenizer stringTokenizer = new StringTokenizer("1,2,3",",");
        // StringTokenizer实现了Enumeration;在Enumeration中支持类似于迭代器的效果 类似于Iterator
        // 而Iterator 替代了 Enumeration在集合体系中的作用,并提供了移除的操作
        while (stringTokenizer.hasMoreTokens()){
            System.out.println(stringTokenizer.nextToken());
        }
    }

    private static void hashTableVsHashMap() {
        // HashTable 通过synchronized保证了所有的操作都是线程安全的
        // 其继承了Dictionary以及实现了Map
        // key和value都不允许为空

        // HashMap 中写操作是线程不安全的
        // 其实现了Map
        // key和value都允许为空

        // ConcurrentHashMap
        // key和value都不允许为空
        // 在写入操作时
        //  当key不存在时,使用cas操作写入数据
        //  如果key存在但其hash已被标记为已删除时,会先删除数据,然后在重新执行写入操作
        //  如果key存在,但不允许覆盖写入时,则直接返回
        //  如果key存在且允许覆盖写入则使用synchronized包裹代码块保证数据覆盖写入线程安全
        // 在读取操作时
        //  由于使用cas查询操作,则有可能存在读取到null的情况,因此对于读取操作而言,并不清楚读取到的数据值本身就是null还是由于并发情况下未读取到数据;因此不允许存储为null的value

        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        // 先初始化数据,在写入数据
        concurrentHashMap.put("1","1");
    }

    private static void vectorVsStack() {
        // Vector 是FIFO
        // Stack 是 LIFO,是Vector的子类; 但Stack不允许在创建时指定容量,由于Vector的默认容量为10,因此Stack的默认容量也为10,当数据量过多时,则会频繁发生扩容操作
    }

    // vector和list对比,这里的list主要指ArrayList
    public static void vectorVsList() {
        // Vector 实现了List,底层也是数组实现
        // Vector 中所有的操作都使用了synchronized 保证了线程安全;而ArrayList是线程不安全的
        // 但对于线程安全而言,vector如果不存在共享以及多线程操作,则vector的所有操作并不存在线程等待消耗,
        // 实际vector的add操作和ArrayList的add操作在时间效率上是一样的
        Vector<String> vector = new Vector<>();
        vector.add("a");
        List<String> stringList = new ArrayList<>();
        stringList.add("a");
    }
}
