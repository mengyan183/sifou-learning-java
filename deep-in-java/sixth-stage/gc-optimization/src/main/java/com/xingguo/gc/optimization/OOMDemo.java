/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.gc.optimization;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * OOMDemo
 * 不同场景OOM案例
 * JDK8
 *
 * @author guoxing
 * @date 2020/10/28 3:14 PM
 * @since
 */
public class OOMDemo {

    /**
     * -XX:+PrintGCDetails 打印gc详情
     * -XX:+PrintGCTimeStamps 打印 每次gc的时间
     *
     * @param args
     */
    public static void main(String[] args) {
//        oomJavaHeapSpace();
//        oomGCOverHead();
        oomUnableCreateNewNativeThread();
    }

    /**
     * 一般发生的问题原因主要为不断手动创建线程而非使用线程池
     * java.lang.OutOfMemoryError:Unable to create new native thread
     */
    public static void oomUnableCreateNewNativeThread() {
        while (true) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * 一般为频繁生成新的对象,且老对象一直被回收
     * jvm启动命令为 -Xmx10m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
     * <p>
     * java.lang.OutOfMemoryError: GC overhead limit exceeded
     * https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/parallel.html#parallel_collector_excessive_gc
     * Excessive GC Time and OutOfMemoryError
     * The parallel collector throws an OutOfMemoryError if too much time is being spent in garbage collection (GC): If more than 98% of the total time is spent in garbage collection and less than 2% of the heap is recovered, then an OutOfMemoryError is thrown. This feature is designed to prevent applications from running for an extended period of time while making little or no progress because the heap is too small. If necessary, this feature can be disabled by adding the option -XX:-UseGCOverheadLimit to the command line.
     * 根据官方的解释为,当heap中不足2%的空间可被回收,且在当前JVM进程,gc的时间超过了98%
     * 忽略当前限制可以使用 -XX:-UseGCOverheadLimit,但也有可能抛出 heap 区域空间不足异常
     * <p>
     * gc日志显示:
     * 1.607: [Full GC (Ergonomics) [PSYoungGen: 1024K->0K(1536K)] [ParOldGen: 4048K->403K(4096K)] 5072K->403K(5632K), [Metaspace: 3256K->3256K(1056768K)], 0.0026587 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
     */
    public static void oomGCOverHead() {
        // 对于 以下 map而言, HashMap/TreeMap/IdentityHashMap,由于插入数据时key 的比较,较为容易出现该问题
        Map<String, String> stringMap = new HashMap<>();
        Random random = new Random();
        while (true) {
            stringMap.put(String.valueOf(random.nextInt()), String.valueOf(random.nextInt()));
        }

    }

    /**
     * 一般为分配新的对象时,内存不足导致的;
     * <p>
     * java.lang.OutOfMemoryError: Java heap space
     * 启动参数
     * -Xmx10m -XX:+PrintFlagsFinal -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
     * gc日志
     * 0.094: [GC (Allocation Failure) [PSYoungGen: 1508K->496K(2560K)] 1508K->560K(9728K), 0.0007552 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
     * 0.094: [GC (Allocation Failure) [PSYoungGen: 496K->496K(2560K)] 560K->576K(9728K), 0.0006032 secs] [Times: user=0.00 sys=0.01, real=0.00 secs]
     * 0.095: [Full GC (Allocation Failure) [PSYoungGen: 496K->0K(2560K)] [ParOldGen: 80K->361K(5632K)] 576K->361K(8192K), [Metaspace: 3037K->3037K(1056768K)], 0.0030636 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
     * 0.098: [GC (Allocation Failure) [PSYoungGen: 0K->0K(2560K)] 361K->361K(9728K), 0.0003739 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     * 0.099: [Full GC (Allocation Failure) [PSYoungGen: 0K->0K(2560K)] [ParOldGen: 361K->344K(5632K)] 361K->344K(8192K), [Metaspace: 3037K->3037K(1056768K)], 0.0025102 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
     * 首先由于分配失败触发young gc,之后经过full gc 仍然分配失败
     */
    public static void oomJavaHeapSpace() {
        int size = 10 * 1000 * 1000;
        // 当前数组需要申请的空间为
        // int 类型 32bits = 4 byte
        // 4byte * 10000000 = 40M 非标准计算
        // 设置 -Xmx10m
        int[] ints = new int[size];
    }
}
