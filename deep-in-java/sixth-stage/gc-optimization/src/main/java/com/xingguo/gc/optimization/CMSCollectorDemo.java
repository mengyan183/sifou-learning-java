/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.gc.optimization;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * CMSCollectorDemo
 * <a href="https://docs.oracle.com/javase/9/gctuning/concurrent-mark-sweep-cms-collector.htm#JSGCT-GUID-4CB5DCEB-FCBF-4A57-83A1-F2C47BF0B3D7"/>
 * <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/cms.html"/>
 *
 * @author guoxing
 * @date 2020/10/29 10:11 AM
 * @since
 */
public class CMSCollectorDemo {

    /**
     * 开启 cms
     * XX:+UseConcMarkSweepGC
     *
     * @param args
     */
    public static void main(String[] args) {
        cmsConcurrentModeFailure();
    }

    /**
     * 启动参数
     * -Xmx120m
     * -XX:+PrintFlagsFinal
     * -XX:+UseConcMarkSweepGC
     * -XX:+CMSIncrementalMode
     * -XX:+PrintGCDetails
     * -XX:+PrintGCDateStamps
     * -XX:+PrintGCTimeStamps
     * <p>
     * GCHeapFreeLimit 设置的默认值为 2,限制fullGC的触发阈值
     * <p>
     * -XX:CMSInitiatingOccupancyFraction 动态调节major gc的触发阈值;默认为-1,表示在运行过程中动态调整
     * gc日志
     * 2020-10-29T10:22:47.733-0800: 0.160: [GC (Allocation Failure) 2020-10-29T10:22:47.733-0800: 0.160: [ParNew: 32768K->4096K(36864K), 0.1772963 secs] 32768K->30583K(118784K), 0.1773471 secs] [Times: user=0.51 sys=0.13, real=0.18 secs]
     * 2020-10-29T10:22:47.957-0800: 0.384: [GC (Allocation Failure) 2020-10-29T10:22:47.957-0800: 0.384: [ParNew: 36864K->4096K(36864K), 0.0285342 secs] 63351K->61296K(118784K), 0.0285705 secs] [Times: user=0.29 sys=0.02, real=0.03 secs]
     * 2020-10-29T10:22:47.985-0800: 0.413: [GC (CMS Initial Mark) [1 CMS-initial-mark: 57200K(81920K)] 61943K(118784K), 0.0005298 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     * 2020-10-29T10:22:47.986-0800: 0.414: [CMS-concurrent-mark-start]
     * 2020-10-29T10:22:48.009-0800: 0.436: [CMS-concurrent-mark: 0.001/0.022 secs] [Times: user=0.03 sys=0.00, real=0.02 secs]
     * 2020-10-29T10:22:48.009-0800: 0.436: [CMS-concurrent-preclean-start]
     * // 重点 第一次 concurrent mode failure时,可以看到 在young generation ParNew: 36864K(36864K)区域占比 100%;对于 old generation 91970K(118784K)区域占比78%
     * // 此时由于出现Allocation Failure 导致full gc产生因此concurrent mode failure失效
     * 2020-10-29T10:22:48.034-0800: 0.461: [GC (Allocation Failure) 2020-10-29T10:22:48.034-0800: 0.461: [ParNew: 36864K->36864K(36864K), 0.0000180 secs]2020-10-29T10:22:48.034-0800: 0.461: [CMS2020-10-29T10:22:48.038-0800: 0.466: [CMS-concurrent-preclean: 0.007/0.030 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
     * (concurrent mode failure): 57200K->81919K(81920K), 0.2346367 secs] 94064K->91970K(118784K), [Metaspace: 3185K->3185K(1056768K)], 0.2346955 secs] [Times: user=0.23 sys=0.01, real=0.23 secs]
     * 2020-10-29T10:22:48.329-0800: 0.757: [Full GC (Allocation Failure) 2020-10-29T10:22:48.329-0800: 0.757: [CMS: 81919K->81919K(81920K), 0.2999121 secs] 118783K->118779K(118784K), [Metaspace: 3185K->3185K(1056768K)], 0.2999514 secs] [Times: user=0.30 sys=0.00, real=0.30 secs]
     * 2020-10-29T10:22:48.629-0800: 1.057: [GC (CMS Initial Mark) [1 CMS-initial-mark: 81919K(81920K)] 118780K(118784K), 0.0202986 secs] [Times: user=0.02 sys=0.00, real=0.03 secs]
     * 2020-10-29T10:22:48.650-0800: 1.077: [CMS-concurrent-mark-start]
     * 2020-10-29T10:22:48.650-0800: 1.078: [Full GC (Allocation Failure) 2020-10-29T10:22:48.650-0800: 1.078: [CMS2020-10-29T10:22:48.710-0800: 1.137: [CMS-concurrent-mark: 0.059/0.060 secs] [Times: user=0.23 sys=0.00, real=0.06 secs]
     * (concurrent mode failure): 81919K->81919K(81920K), 0.3100415 secs] 118783K->118783K(118784K), [Metaspace: 3185K->3185K(1056768K)], 0.3100896 secs] [Times: user=0.48 sys=0.00, real=0.31 secs]
     * 2020-10-29T10:22:48.960-0800: 1.388: [Full GC (Allocation Failure) 2020-10-29T10:22:48.960-0800: 1.388: [CMS: 81919K->81920K(81920K), 0.2856253 secs] 118783K->118764K(118784K), [Metaspace: 3185K->3185K(1056768K)], 0.2856565 secs] [Times: user=0.28 sys=0.00, real=0.28 secs]
     * 2020-10-29T10:22:49.246-0800: 1.674: [Full GC (Allocation Failure) 2020-10-29T10:22:49.246-0800: 1.674: [CMS: 81920K->81920K(81920K), 0.2684464 secs] 118784K->118784K(118784K), [Metaspace: 3185K->3185K(1056768K)], 0.2684809 secs] [Times: user=0.27 sys=0.00, real=0.27 secs]
     * 2020-10-29T10:22:49.515-0800: 1.942: [Full GC (Allocation Failure) 2020-10-29T10:22:49.515-0800: 1.942: [CMS: 81920K->81920K(81920K), 0.2678815 secs] 118784K->118784K(118784K), [Metaspace: 3185K->3185K(1056768K)], 0.2679153 secs] [Times: user=0.27 sys=0.00, real=0.27 secs]
     * 2020-10-29T10:22:49.783-0800: 2.210: [GC (CMS Initial Mark) [1 CMS-initial-mark: 81920K(81920K)] 118784K(118784K), 0.0214997 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
     * 2020-10-29T10:22:49.804-0800: 2.232: [CMS-concurrent-mark-start]
     * 2020-10-29T10:22:49.804-0800: 2.232: [Full GC (Allocation Failure) 2020-10-29T10:22:49.804-0800: 2.232: [CMS2020-10-29T10:22:49.864-0800: 2.291: [CMS-concurrent-mark: 0.058/0.059 secs] [Times: user=0.23 sys=0.01, real=0.06 secs]
     * (concurrent mode failure): 81920K->374K(81920K), 0.0922428 secs] 118784K->374K(118784K), [Metaspace: 3185K->3185K(1056768K)], 0.0922735 secs] [Times: user=0.27 sys=0.01, real=0.09 secs]
     * Heap
     * par new generation   total 36864K, used 1923K [0x00000007b8800000, 0x00000007bb000000, 0x00000007bb000000)
     * eden space 32768K,   5% used [0x00000007b8800000, 0x00000007b89e0e68, 0x00000007ba800000)
     * from space 4096K,   0% used [0x00000007ba800000, 0x00000007ba800000, 0x00000007bac00000)
     * to   space 4096K,   0% used [0x00000007bac00000, 0x00000007bac00000, 0x00000007bb000000)
     * concurrent mark-sweep generation total 81920K, used 374K [0x00000007bb000000, 0x00000007c0000000, 0x00000007c0000000)
     * Metaspace       used 3273K, capacity 4496K, committed 4864K, reserved 1056768K
     * class space    used 354K, capacity 388K, committed 512K, reserved 1048576K
     */
    public static void cmsConcurrentModeFailure() {
        Map<String, String> stringMap = new HashMap<>();
        Random random = new Random();
        while (true) {
            stringMap.put(String.valueOf(random.nextInt()), String.valueOf(random.nextInt()));
        }
    }
}
