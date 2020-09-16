/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection;

import org.apache.commons.collections.map.LRUMap;

import java.util.*;
import java.util.stream.Stream;

/**
 * SetAndMapDemo
 *
 * @author guoxing
 * @date 9/15/20 9:24 PM
 * @since
 */
public class SetAndMapDemo {
    public static void main(String[] args) {
        // 在HashSet的无参构造方法中实际就是创建了一个hashMap,在hashSet添加数据时实际就是调用的HashMap的put方法,新增值作为key,而对应的value是一个常量对象
        // 对于hashMap的key实际是进行了hash运算进行数据存储
        Set<String> stringSet = new HashSet<>();
        stringSet.add("A");
        // Set是Map key的底层实现;而Set的底层实际也用到了HashMap
        Map<String, String> stringMap = new HashMap<>();
        // 和hash对应的tree结构使用的是二叉树索引而非hash索引
        stringSet = new TreeSet<>();
        // 对于treeSet底层实际是使用的treeMap数据结构进行数据存储
        // 在存储数据时,当key不为null时
        // 如果未指定compare断言则,首先判断key的类型是否实现了comparable接口,如果实现了comparable接口,则使用compareto方法进行比较按照从小到大的顺序进行插入数据,按照 左<根<右;如果compare结果为0,说明当前key已存在,并覆盖当前已存在的key对应的value
        // 如果指定了compare 则使用指定的compare进行比较;但如果compare的比较结果不是默认的比较,则最终生成的树的节点顺序则不一定是 左<根<右
        // 例如 Integer/String 都实现了comparable 接口
        Integer[] integers = new Integer[]{3, 1, 2, 2};
        Set<Integer> integerSet = new TreeSet<>(Arrays.asList(integers));
        // 可以看到结果是去重后的有序数据
        integerSet.forEach(System.out::println);
        // 当指定compare时
        integerSet = new TreeSet<>((x, y) -> (x < y) ? 1 : ((x.equals(y)) ? 0 : -1));
        integerSet.addAll(Arrays.asList(integers));
        integerSet.forEach(System.out::println);
        System.out.println("==================");
        // 对于linkedHashMap可以通过 accessOrder 参数可以控制数据顺序
        // 当accessOrder == false 时,默认按照插入顺序
        // 当accessOrder == true 时,按照读取频率顺序,读取次数越多,顺序越靠前
        // 通过 accessOrder == true 可以实现LRU淘汰缓存策略
        Object o = new Object();
        Map<String, Object> stringIntegerMap = new LinkedHashMap<>();
        stringIntegerMap.put("1", o);
        stringIntegerMap.put("2", o);
        stringIntegerMap.put("3", o);
        // 可以利用commons包下拓展的LRUMap
        LRUMap lruMap = new LRUMap(stringIntegerMap);
        lruMap.forEach((k, v) -> {
            System.out.println(k);
        });
        System.out.println("============");
        lruMap.get("3");
        lruMap.forEach((k, v) -> {
            System.out.println(k);
        });
        System.out.println("==============");
        lruMap.get("2");
        lruMap.forEach((k, v) -> {
            System.out.println(k);
        });
    }
}
