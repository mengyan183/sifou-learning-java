/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional.stream;

import java.util.*;
import java.util.stream.Collectors;

/**
 * StreamDemo
 * Stream 操作
 *
 * @author guoxing
 * @date 9/14/20 4:17 PM
 * @since
 */
public class StreamDemo {
    public static void main(String[] args) {
        sum(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        List<String> stringList = mapToString(Arrays.asList(1, 2, 3, 4, 5, 6));
        System.out.println(stringList);
        // 正序
        System.out.println(sorted(Arrays.asList(9, 8, 342, 432, 52, 6)));
        // 倒序
        System.out.println(sorted(Arrays.asList(1, 2, 3, 4, 5, 6),
                (x, y) -> (x < y) ? 1 : ((x.equals(y)) ? 0 : -1)));
    }

    public static List<Integer> sorted(List<Integer> list) {
        return new ArrayList<>(list).stream().sorted().collect(Collectors.toList());
    }

    public static List<Integer> sorted(List<Integer> list, Comparator<Integer> comparator) {
        return new ArrayList<>(list).stream().sorted(comparator).collect(Collectors.toList());
    }

    // 进行转换操作
    public static List<String> mapToString(List<Integer> integers) {
        return new ArrayList<>(integers).stream().map(Object::toString).collect(Collectors.toList());
    }

    // 将集合中的数据执行相加操作
    public static void sum(Collection<Integer> integerCollection) {
        integerCollection.stream()// 转换为数据流
                .reduce(Integer::sum) //对流中的数据执行相加操作
                .ifPresent(System.out::println); // 对结果Optional 进行输出打印
    }
}
