/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.gc.optimization;

import java.util.HashMap;
import java.util.Random;

/**
 * GarbageFirstCollectorDemo
 *
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
     * jdk 8 版本 G1调优
     * -XX:+PrintGCDetails
     * // 以下两个参数JDK15不再支持
     * -XX:+PrintGCDateStamps
     * -XX:+PrintGCTimeStamps
     * 开启G1
     * 启动参数
     * -XX:+UseG1GC 开启G1
     * -XX:G1HeapRegionSize=2 设置G1region的区域大小为2m
     * -XX:+UnlockExperimentalVMOptions 解锁实验性功能
     * -XX:G1NewSizePercent=  设置young generation 区域初始化大小; 会覆盖 -Xmn
     * -XX:G1MaxNewSizePercent= 设置young generation 区域最大占用heap比例大小; 会覆盖 -Xmx
     * -XX:InitiatingHeapOccupancyPercent= 设置开始执行initialMark的收集触发阈值(简称IHOP),为老年代空间定义heap占用比例
     * -XX:+G1UseAdaptiveIHOP 默认情况下,G1会根据运行期中的标记时间和标记周期来动态调整 IHOP值
     * -XX:MaxGCPauseMillis= 限定GC最大停顿时间,该时间的调整会相对应的影响到吞吐量
     * -XX:MaxGCMinorPauseMillis = 限定minor gc最大停顿时间
     * -XX:GCPauseIntervalMillis = 设置停顿时间间隔
     *
     */
    /**
     * JDK15
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
