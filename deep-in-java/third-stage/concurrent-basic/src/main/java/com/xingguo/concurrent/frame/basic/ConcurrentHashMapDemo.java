/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMapDemo
 *
 * @author guoxing
 * @date 2020/9/29 2:46 PM
 * @since
 */
public class ConcurrentHashMapDemo {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, Integer> demo = new ConcurrentHashMap<>();
        for (int i = 0; i < 100; i++) {
            demo.put(i, i);
        }
    }
}
