/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection;

import java.util.LinkedList;
import java.util.Queue;

/**
 * QueueOrDequeDemo
 *
 * @author guoxing
 * @date 9/15/20 9:12 PM
 * @since
 */
public class QueueOrDequeDemo {

    public static void main(String[] args) {
        // 对于 LinkedList ,add和offer没有区别
        Queue<String> stringQueue = new LinkedList<>();
        stringQueue.add("A");
        stringQueue.offer("B");
    }
}
