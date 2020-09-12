/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * UnmodifiableDemo
 *
 * @author guoxing
 * @date 9/11/20 9:32 PM
 * @since
 */
public class UnmodifiableDemo {

    public static void main(String[] args) {
        // 返回一个支持修改的集合
        Collection<Integer> of = of(1, 2, 3, 4);
        of.add(5);

        // 返回一个只允许读不允许写的集合
        Collection<Integer> integers = unmodifiableOf(1, 2, 3, 4, 5, 6);
        // 当执行写操作时 会抛出异常
        // 对于当前的integers 实际是线程安全的,因为限制了只允许只读
        // 对于 线程安全的定义实际 是要求 读/写的一致性 ; 因此对于线程安全的分析也应该根据具体的使用场景
        integers.add(7);

        // 对于当前CopyOnWriteArrayList 中保证读写一致, 在写入操作时实际是利用的快照数据
        // 在写的过程中首先进行加锁操作,避免存在并发写的情况
        // 且对于写入操作,为了避免数据写入导致出现幻读; 首先会对当前list的数组数据进行一份数据复制操作,在新的数组里面执行添加元素的操作
        // 当在新的数组中成功添加元素后,再将新的数组赋值给当前list,保证了读写的一致性
        CopyOnWriteArrayList<Integer> integers1 = new CopyOnWriteArrayList<>();
        integers1.add(1);

    }

    public static Collection<Integer> of(Integer... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

    public static Collection<Integer> unmodifiableOf(Integer... values) {
        return Collections.unmodifiableCollection(Arrays.asList(values));
    }
}
