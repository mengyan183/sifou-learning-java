/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * ConcurrentSkipListDemo
 *
 * @author guoxing
 * @date 2020/9/29 1:01 PM
 * @since
 */
@Slf4j
public class ConcurrentSkipListDemo {
    public static void main(String[] args) {
        // 支持并发操作,但是没有锁操作, 在执行写入操作时,通过手动内存屏障(VarHandle.acquireFence();)保证happen-before
        // 对于写入的key支持按照指定的comparator比较保证数据的顺序性
        // 且支持skipList的特点,因此其相关操作的时间复杂度均为O(logn)
        // 跳表的本质实际是利用了空间换时间的特点, 且跳表的存储结构为链表
        ConcurrentSkipListMap<String, Integer> stringIntegerConcurrentSkipListMap = new ConcurrentSkipListMap<>();
        stringIntegerConcurrentSkipListMap.put("A", 1);
        stringIntegerConcurrentSkipListMap.put("B", 2);
        log.info("{}", stringIntegerConcurrentSkipListMap);
        // 对于当前的size操作并不是一个常量操作,且map实际是链表结构,长度是无限的,由于int类型的局限性,当数据超过时,只会返回int的最大值
        log.info("{}",stringIntegerConcurrentSkipListMap.size());
        // 对于所有的批量操作,并发集合框架都不能保证数据一致性,原因在于,对于批量操作时实际都是快照数据
        stringIntegerConcurrentSkipListMap.putAll(new HashMap<>());
    }
}
