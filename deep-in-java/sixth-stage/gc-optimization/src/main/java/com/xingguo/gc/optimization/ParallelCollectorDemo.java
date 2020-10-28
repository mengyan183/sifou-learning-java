/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.gc.optimization;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * ParallelCollectorDemo
 *
 * @author guoxing
 * @date 2020/10/28 10:31 AM
 * @since
 */
@Slf4j
public class ParallelCollectorDemo {
    /**
     * -XX:+UseParallelGC  // young generation 使用parallel gc
     * -XX:+UseParallelOldGC // 所有的generation 都使用 parallel gc ;对于 old generation 是使用的 parallel compacting collector; 该参数在 jdk 15中被移除
     * -XX:+PrintFlagsFinal // 打印全部的gc参数信息
     * -XX:ParallelGCThreads= 并行线程数 : 当可用线程数大于8时,默认为 大约为 可用线程数 * 5 / 8; 当可用线程数小于等于8时,默认为 可用线程数 ; 本机测试 由于Intel超线程技术,最大可用线程为16,ParallelGCThreads为13
     * -XX:MaxGCPauseMillis=停顿时间毫秒 : jdk8 默认值为 18446744073709551615 而 JDK15 为 200
     * -XX:GCTimeRatio=正整数 : jdk8默认为为 99,表示gc时间占用整个JVM存活时间的比率, 计算方法为 1/(GCTimeRatio+1);吞吐量
     * <p>
     * 分代相关
     * 新生代扩容比率: -XX:YoungGenerationSizeIncrement=正整数 默认值为 20
     * 老年代扩容比率: -XX:TenuredGenerationSizeIncrement=正整数 默认值为 20
     * 相对应的缩容比率通过 -XX:AdaptiveSizeDecrementScaleFactor=正整数 来调节,默认为4;通过扩容比率除以当前值
     * <p>
     * 当前机器 16g内存测试结果
     * heap内存
     * 初始化内存 InitialHeapSize 默认为 本机内存的1/64 ;设置参数为 -Xms数值+单位
     * 最大内存 MaxHeapSize 默认为 本机内存的 1/4 ; 设置参数为 -Xmx数值+单位
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        int i = Runtime.getRuntime().availableProcessors();
        log.info("可用线程个数为:{}", i);
        System.in.read();
    }
}
