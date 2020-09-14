/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional;

import java.util.*;
import java.util.function.Predicate;

/**
 * PredicateDesignDemo
 * predicate 设计
 *
 * @author guoxing
 * @date 9/14/20 3:54 PM
 * @since
 */
public class PredicateDesignDemo {

    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        // 获取全部的偶数
        System.out.println(filter(integers, v -> v % 2 == 0));
        // 获取全部的奇数
        System.out.println(filter(integers, v -> v % 2 != 0));

        // 使用Stream进行操作
        integers.stream().filter(v -> v % 2 == 0).forEach(System.out::println);
    }

    /**
     * 对于当前集合数据根据传递的判断进行数据过滤
     *
     * @param collection
     * @param predicate
     * @param <T>
     * @return 只读集合数据
     */
    public static <T> Collection<T> filter(Collection<T> collection, Predicate<T> predicate) {
        // 为了避免操作原始数据,或原始数据中的部分限制; 对传入的集合数据首先复制一份快照数据
        Collection<T> snapShotCollection = new ArrayList<>(collection);
        // 转换为迭代类型数据,根据判断条件对数据进行过滤,如果不满足条件的则进行删除操作
        snapShotCollection.removeIf(t -> !predicate.test(t));
        // 返回只读数据
        return Collections.unmodifiableCollection(snapShotCollection);
    }
}
