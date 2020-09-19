/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.algorithms;

import java.util.stream.Stream;

/**
 * BubbleSort
 * 冒泡排序实际是一种原地排序算法,不需要额外的内存空间
 *
 * @author guoxing
 * @date 9/19/20 11:04 AM
 * @since
 */
public class BubbleSort<T extends Comparable<T>> implements Sort<T> {

    @Override
    public void sort(T[] array, boolean flag) {
        if (array == null || array.length < 2) {
            return;
        }
        int length = array.length;
        //
        // 对于Comparable.compareTo 默认
        // < -1
        // = 0
        // > 1
        // 每一次排序都能保证有一个元素在最终位置
        // i 表示总共要冒泡的次数
        for (int i = 0; i < length - 1; i++) {
            // 限制第一个元素要对比的数量, 由于每执行一次冒泡,都能保证肯定会有一个元素在其最终位置,因此下一次冒泡时就不需要在操作该元素,因此j < length - 1 - i
            /**
             * 假设存在以下元素
             *  5    3   2   1  需要进行升序排序
             *  j
             * 第一次冒泡
             *  5  > 3 ,需要对 5和 3进行交换位置
             *  3   5   2   1
             *      j
             *      5 > 2 , 需要对5 和 2 进行交换位置
             *  3   2   5   1
             *          j
             *          5  > 1 需要对 5 和 1进行交换位置
             *  3   2   1   5
             *              j 此时 j 走到了尽头,代表当前一次冒泡结束; 对于j而言,每次都是指向当前已比较的最大的数;
             *              每一次冒泡都能保证肯定有一个元素在其最终位置上,因此对于下一次的冒泡对比的元素的个数就应该比上一次 - 1
             */
            for (int j = 0; j < length - 1 - i; j++) {
                if ((flag && array[j].compareTo(array[j + 1]) == 1) || (!flag && array[j].compareTo(array[j + 1]) == -1)) {
                    T v = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = v;
                }
            }
        }
    }

    public static void main(String[] args) {
        Integer[] values = new Integer[]{5, 3, 2, 1};
        BubbleSort<Integer> integerBubbleSort = new BubbleSort<>();
        integerBubbleSort.sort(values, true);
        Stream.of(values).forEach(System.out::print);
        System.out.println();
        integerBubbleSort.sort(values, false);
        Stream.of(values).forEach(System.out::print);
    }
}
