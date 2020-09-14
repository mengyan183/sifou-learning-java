/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional.stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * StreamCollectDemo
 *
 * @author guoxing
 * @date 9/14/20 5:20 PM
 * @since
 */
public class StreamCollectDemo {
    public static void main(String[] args) {
        Stream.of(1, 2, 3, 4).collect(Collectors.summingInt(Integer::intValue));
        Stream.of(1, 2, 3, 4).mapToInt(Integer::intValue).sum();
        Map<Integer, List<Integer>> listMap = Stream.of(1, 2, 3, 4, 5)
                .collect(Collectors.groupingBy(Integer::intValue));
        // 将二维转换为一维数据
        List<Integer> collect = listMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        // 对流中的全部数据执行相加操作,如果结果不为空则数据结果
        collect.stream().reduce(Integer::sum).ifPresent(System.out::println);
    }
}
