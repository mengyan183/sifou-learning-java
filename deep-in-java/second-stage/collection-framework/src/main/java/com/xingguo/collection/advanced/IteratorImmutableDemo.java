/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.advanced;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * IteratorImmutableDemo
 * 迭代器的不可变性
 *
 * @author guoxing
 * @date 9/17/20 7:54 PM
 * @since
 */
public class IteratorImmutableDemo {

    public static void main(String[] args) {
        ArrayList<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        // 对于使用迭代器过程中不允许操作原始数据集合
        Iterator<Integer> iterator = integers.iterator();
// 迭代器的不可变是要求集合的元素个数不可变
        while (iterator.hasNext()) {
            // 在 调用 Iterator#next 之前调用集合的add方法,则在调用 next方法时会抛出ConcurrentModificationException;
            //原因时监测到集合容器中的元素数量发生变化
//            integers.add(1);
            // 当执行编辑操作时不会抛出异常
            integers.set(1, 1);
            //当游标尚未运行时,此时还未出现被浏览过的数据,则会抛出 java.lang.IllegalStateException
            // 当删除的数量超过当前已读取的数量时 就会抛出异常
            // 删除实际是删除的上一个读取到的元素
//            iterator.remove();
//            integers.remove(0); // 由于会导致集合元素长度发生变化因此也会抛出并发修改异常
            Integer next = iterator.next();
//            integers.add(next);
        }
    }
}
