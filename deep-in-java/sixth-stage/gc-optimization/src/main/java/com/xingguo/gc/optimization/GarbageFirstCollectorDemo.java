/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.gc.optimization;

import java.util.HashMap;
import java.util.Random;

/**
 * GarbageFirstCollectorDemo
 * jdk 8 版本 G1调优
 *
 * @author guoxing
 * @date 2020/10/30 2:41 PM
 * @since
 */
public class GarbageFirstCollectorDemo {
    public static void main(String[] args) {
        openG1();
    }

    /**
     * 开启G1
     * 启动参数
     * -XX:+UseG1GC 开启G1
     * -XX:G1HeapRegionSize=2 设置G1region的区域大小为2m
     * -XX:+UnlockExperimentalVMOptions 解锁实验性功能
     * -XX:G1NewSizePercent=  设置young generation 区域初始化大小; 会覆盖 -Xmn
     * -XX:G1MaxNewSizePercent= 设置young generation 区域最大占用heap比例大小; 会覆盖 -Xmx
     *
     */
    public static void openG1() {
        System.gc();
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        Random random = new Random();
        while (true) {
            stringStringHashMap.put(String.valueOf(random.nextInt()), String.valueOf(random.nextInt()));
        }
    }
}
