/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.gc.optimization;

import lombok.extern.slf4j.Slf4j;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * ReferenceDemo
 *
 * @author guoxing
 * @date 2020/11/7 4:52 PM
 * @since
 */
@Slf4j
public class ReferenceDemo {
    /**
     * -XX:+PrintFlagsFinal
     * -Xlog:gc*=debug
     */
    /**
     * 对于GC中的引用分析
     *
     * @param args
     */
    public static void main(String[] args) {
        /**
         * 当前引用属于强引用,且当前属于方法内部局部变量,在方法执行结束后,引用就消失了
         */
        Integer i = Integer.valueOf(128);

        /**
         * [0.102s][debug][gc,phases,ref  ] GC(0)   SoftReference:
         * [0.102s][debug][gc,phases,ref  ] GC(0)     Discovered: 0
         * [0.102s][debug][gc,phases,ref  ] GC(0)     Cleared: 0
         * [0.102s][debug][gc,phases,ref  ] GC(0)   WeakReference:
         * [0.102s][debug][gc,phases,ref  ] GC(0)     Discovered: 56
         * [0.102s][debug][gc,phases,ref  ] GC(0)     Cleared: 39
         * [0.102s][debug][gc,phases,ref  ] GC(0)   FinalReference:
         * [0.102s][debug][gc,phases,ref  ] GC(0)     Discovered: 0
         * [0.102s][debug][gc,phases,ref  ] GC(0)     Cleared: 0
         * [0.102s][debug][gc,phases,ref  ] GC(0)   PhantomReference:
         * [0.102s][debug][gc,phases,ref  ] GC(0)     Discovered: 32
         * [0.102s][debug][gc,phases,ref  ] GC(0)     Cleared: 19
         *
         * 根据gc日志中的顺序可以看到 Reference相关优先级
         * SoftReference: 对于当前引用一般适用于内存缓存,当内存不足且发生GC时会回收当前引用的数据
         * WeakReference: 对于当前引用只要发生GC就会将当前数据加入到引用队列中等待gc回收
         * FinalReference:只能包内访问,一般发生在object#finalize阶段
         * PhantomReference
         *
         * 对于 ReferenceQueue 虽然提供了 java.lang.ref.Reference#enqueue() 相关的入队的java方法,但是对于java.lang.ref.Reference#queue 属性实际主要是JVM中GC直接操作,被GC线程调用将数据入队;通过WeakReference可以看出来,当发生gc后,在指定的ReferenceQueue会入队一个元素
         */
        ReferenceQueue<Integer> integerReferenceQueue = new ReferenceQueue<>();
        Reference<Integer> integerSoftReference;
        // memory-sensitive 主要应用于内存敏感的缓存,只有到内存不足时发生gc才会将当前数据回收
//        integerSoftReference = new SoftReference<>(Integer.valueOf(128), integerReferenceQueue);
//        integerSoftReference = new SoftReference<>(i, integerReferenceQueue);
//        integerSoftReference = new WeakReference<>(Integer.valueOf(128), integerReferenceQueue);
        integerSoftReference = new WeakReference<>(i, integerReferenceQueue);
        /**
         * 当 不设置 i = null 时;
         * [main] INFO com.xingguo.gc.optimization.ReferenceDemo - java.lang.ref.WeakReference@1d81eb93:128
         * [main] INFO com.xingguo.gc.optimization.ReferenceDemo - ReferenceQueue:null
         * [main] INFO com.xingguo.gc.optimization.ReferenceDemo - java.lang.ref.WeakReference@1d81eb93:128
         * [main] INFO com.xingguo.gc.optimization.ReferenceDemo - ReferenceQueue:null;value:null
         *
         * 当设置 i = null 时;
         * [main] INFO com.xingguo.gc.optimization.ReferenceDemo - java.lang.ref.WeakReference@1d81eb93:128
         * [main] INFO com.xingguo.gc.optimization.ReferenceDemo - ReferenceQueue:null
         * [main] INFO com.xingguo.gc.optimization.ReferenceDemo - java.lang.ref.WeakReference@1d81eb93:null
         * [main] INFO com.xingguo.gc.optimization.ReferenceDemo - ReferenceQueue:java.lang.ref.WeakReference@1d81eb93;value:null
         *
         * 由于  Integer i = Integer.valueOf(128); 中 i 属于 强引用,且为方法内的局部变量,其生命周期为当前方法周期;
         * 当 不设置 i = null;, 即使在发生gc 时根据引用计数 可以看到 对于  Integer.valueOf(128); 对象肯定存在 变量i引用; 因此gc不会回收当前对象
         * 因此对于 相关的reference对象中引用数据,由于gc数据不会回收当前对象,因此相关的引用永远也不会存放在referenceQueue中;
         *
         *而当设置 了 i = null; 后,当发生gc时,此时由于强引用的失效,因此此时相关的reference对象会根据不同的特性执行不同的回收策略;
         * 对于soft类型而言,其策略是只有在当前堆内存不足时,才会回收当前对象,并将当前引用对象加入到referenceQueue中
         * 对于weak类型而言,其策略是只要发生gc就会导致当前对象被回收,且引用对象被加入到referenceQueue中
         * 对于finalize类型而言,对于这种一般发生在对象的生命结束阶段,当普通类重写finalize方法时,会容易导致要被回收的对象发生强引用,从而导致对象无法被回收,因此一般不建议重写当前方法
         */
        i = null;
        log.info("{}:{}", integerSoftReference, integerSoftReference.get());
        log.info("ReferenceQueue:{}", integerReferenceQueue.poll());
        System.gc();
        log.info("{}:{}", integerSoftReference, integerSoftReference.get());
        Reference<? extends Integer> poll = integerReferenceQueue.poll();
        log.info("ReferenceQueue:{};value:{}", poll, Objects.nonNull(poll) ? poll.get() : null);

        Integer a = Integer.valueOf(0);
        Integer b = a;
        a = null;
        assert b == null;
    }
}
