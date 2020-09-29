/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteDemo
 * 并发集合框架, 写入时会复制一份快照数据
 *
 * @author guoxing
 * @date 2020/9/29 12:14 PM
 * @since
 */
@Slf4j
public class CopyOnWriteDemo {

    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> integers = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 3; i++) {
            /**
             * 当第一次循环进入时,会执行加锁操作,将当前线程id添加到lock的对象头中(cas操作)
             * 当第二次循环进入时,判断当前线程和lock对象头中的线程id是否一致,如果一致则不需要在执行锁竞争操作
             */
            // 每次执行写入操作时,都会先进行加锁 -> 复制数据,生成新的数组 -> 写入数据
            integers.add(i);
        }

        // 对于非线程安全的集合,当在遍历过程中执行add操作时会抛出ConcurrentModificationException ;
        // 而对于当前迭代操作而言,在获取迭代器的时候,实际是将当前集合的数组进行快照数据,因此原集合的修改并不会影响当前迭代器的; 由于是使用的快照数据,因此当前迭代器只支持读取操作,并不支持任何写操作
        Iterator<Integer> iterator = integers.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            log.info("{}",next);
//            iterator.remove(); // 对于当前iterator并没有实现remove方法,会抛出unsupport异常信息
            integers.add(next);
        }
    }
}
