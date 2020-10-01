/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * QuickSortForkJoinPoolDemo
 *
 * @author guoxing
 * @date 2020/10/1 9:27 AM
 * @since
 */
@Slf4j
public class QuickSortForkJoinPoolDemo {
    public static void main(String[] args) throws InterruptedException {
        Integer[] integers = new Integer[20];
        Random random = new Random();
        for (int i = 0; i < integers.length; i++) {
            integers[i] = random.nextInt(100);
        }
        log.info("{}", Arrays.asList(integers));
        new QuickSortTask(integers).sort();
        log.info("{}", Arrays.asList(integers));
    }

}

class QuickSortTask extends RecursiveAction {
    public final Integer[] values;
    public final int low;
    public final int high;
    // 排序的阈值
    public final int THRESHOLD = 4;
    public final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public QuickSortTask(Integer[] values) {
        this.values = values;
        this.low = 0;
        this.high = values.length - 1;
    }

    public QuickSortTask(Integer[] values, int low, int high) {
        this.values = values;
        this.low = low;
        this.high = high;
    }

    public void sort() throws InterruptedException {
        forkJoinPool.submit(this);
        forkJoinPool.awaitTermination(100L, TimeUnit.MILLISECONDS);
        forkJoinPool.shutdown();
    }

    /**
     * The main computation performed by this task.
     */
    @Override
    protected void compute() {
        // 当分区小于阈值时直接进行排序
        if (high - low < THRESHOLD) {
            Arrays.sort(values, low, high);
        } else {
            int pivotIndex = partition(values, low, high);
            //创建异步任务,采用异步任务执行替代递归操作
            forkJoinPool.submit(new QuickSortTask(values, low, pivotIndex)).join();
            forkJoinPool.submit(new QuickSortTask(values, pivotIndex + 1, high)).join();
        }
    }

    private int partition(Integer[] values, int low, int high) {
        // 中位值
        Integer pivot = values[high];
        int pivotIndex = low;
        /**
         * 3125 highValue = 5 i = 0, pivotIndex = 0
         * 3 < 5  pivotIndex++ = 1; i++ = 1
         * 1<5     pivotIndex++ = 2; i++ = 2
         * 2<5      pivotIndex++ = 3; i++ = 3
         * 循环结束;将pivotIndex 和 high 位置元素进行交换
         *
         *
         */
        for (int i = low; i < high; i++) {
            if (values[i].compareTo(pivot) < 1) {
                exchange(values, pivotIndex, i);
                pivotIndex++;
            }
        }
        {
            exchange(values, pivotIndex, high);
        }
        return pivotIndex;
    }

    private void exchange(Integer[] values, int sourceIndex, int destIndex) {
        if (sourceIndex != destIndex) {
            Integer temp = values[sourceIndex];
            values[sourceIndex] = values[destIndex];
            values[destIndex] = temp;
        }
    }
}
