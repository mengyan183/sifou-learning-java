/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.algorithms;

import java.util.stream.Stream;

/**
 * QuickSort
 * 快速排序
 *
 * @author guoxing
 * @date 2020/9/20 10:39 AM
 * @since
 */
public class QuickSort<T extends Comparable<T>> implements Sort<T> {

    /**
     * 快排的主要思想在于分区,通过不断的分区达到排序的效果
     * 5    4   3   2   1
     *                  pivot(中心点)
     * low->开始遍历当前区间high
     * i,j ; 当存在 array[j] < pivot 时, 交换 array[i],array[j] = array[j],array[i] (golang语法),此时i + 1,后移一位,j继续往后移动直到high,交换当前i和high,array[i],array[high] = array[high],array[i] , 此时i就作为当前分区的中心点
     * 第一次分区结束后的结果为 ,当前pivot 的索引为 0(i) ;分成的区间为 [][1][4,3,2,5];由中心点继续分区
     *
     * @param array
     * @param flag  true 代表的是正序;false代表的是逆序
     */
    @Override
    public void sort(T[] array, boolean flag) {
        sort(array, 0, array.length - 1, flag);
    }

    /**
     * 对于快速排序实际就是一个不断递归分区的过程
     *
     * @param array
     * @param low
     * @param high
     * @param flag  true代表升序,false代表降序
     */
    public void sort(T[] array, int low, int high, boolean flag){
        // 如果低位索引值大于等于高位索引值,则表示没有必要在进行继续分区
        if (low >= high) {
            return;
        }
        // 返回分区中间值索引
        int pivotIndex = partition(array,low,high,flag);
        // 递归 左侧分区
        sort(array, low, pivotIndex - 1, flag);
        // 递归 右侧分区
        sort(array, pivotIndex + 1, high, flag);
    }

    public int partition(T[] array, int low, int high, boolean flag) {
        // 最高位作为中心点
        T pivot = array[high];
        // 待交换位置
        int i = low;
        // 遍历游标
        int j = low;
        for (; j < high; j++) {
            // 如果为升序,当存在array[j] =< pivot,将array[j]放到前面和i进行交换位置,并将i后移一位
            if ((flag && array[j].compareTo(pivot) < 1) || (!flag && array[j].compareTo(pivot) > 0)) {
                // 交换i和j的元素
                T temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                // 将i后移一位
                i++;
            }
        }
        // 不管i是否变化,都要将中心点元素和当前i所在位置元素进行交换
        // 交换当前high 和 i 的元素,将中间点的元素移动到中间索引位置
        array[high] = array[i];
        array[i] = pivot;
        // 返回中间索引
        return i;
    }

    public static void main(String[] args) {
        Integer[] values = new Integer[]{5, 3, 2, 1};
        Sort<Integer> integerInsertSort = new QuickSort<>();
        integerInsertSort.sort(values, true);
        Stream.of(values).forEach(System.out::print);
        System.out.println();
        integerInsertSort.sort(values, false);
        Stream.of(values).forEach(System.out::print);
    }
}
