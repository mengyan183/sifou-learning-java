/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.advanced;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * FailFastVsFailSafeDemo
 * 快速失败和安全失败
 *
 * @author guoxing
 * @date 9/17/20 8:11 PM
 * @since
 */
public class FailFastVsFailSafeDemo {

    public static void main(String[] args) {
        // 对于快速失败,当操作失败时,则立即终止整个操作
        failFastDemo();
        failSafeDemo();
    }

    private static void failSafeDemo() {
        CopyOnWriteArrayList<Integer> integers = new CopyOnWriteArrayList<>(Arrays.asList(1, 2, 3, 4));
        remove(integers);
    }

    private static void failFastDemo() {
        ArrayList<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        remove(integers);
    }

    private static void remove(Collection<?> values) {
        try {
            for (Object value : values) {
                values.remove(value);
            }
        } catch (ConcurrentModificationException e) {
            System.err.println(e.getMessage());
        }
        System.out.println(values.size());
    }
}
