/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.algorithms;

import java.util.Arrays;
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
     *  pivot元素初始时是当前分区的最高位元素,high为pivot元素前一个位置,low为最低位
     *  通过low high双指针实现对比交换
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
//        int pivotIndex = partition(array,low,high,flag);
        // 双指针移动, low和high
        int pivotIndex = partition2(array,low,high,flag);
        // 对于左右分区都不会包含中间元素
        // 递归 左侧分区
        sort(array, low, pivotIndex - 1, flag);
        // 递归 右侧分区
        sort(array, pivotIndex + 1, high, flag);
    }

    // 双指针,通过块慢指针均从低位移动, 对于[0,i]区间可以保证的是其中的元素肯定都大于等于或小于等于中间元素
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

    // 采用双指针移动, 低位指针从低往高位移动,高位指针从高位往低位移动
    public int partition2(T[] array, int low, int high, boolean flag) {
        // 将最高位设置为pivot元素
        T pivot = array[high];
        while (low < high) {
            if ((flag && array[low].compareTo(pivot) == -1) || (!flag && array[low].compareTo(pivot) == 1)) {
                low++;
            } else {
                // 交换高位和低位元素
                array[high--] = array[low];
                // 此时high已经前移一位
                array[low] = array[high];
                // 此时high的位置已经出现空闲插槽
            }
        }
        // 将中间元素放入空闲插槽中
        array[high] = pivot;
        return high;
    }

    public static void main(String[] args) {
        Integer[] values = new Integer[]{3, 7, 8, 5, 2, 1, 5, 4};
        Sort<Integer> integerInsertSort = new QuickSort<>();
        integerInsertSort.sort(values, true);
        Stream.of(values).forEach(System.out::print);
        System.out.println();
        integerInsertSort.sort(values, false);
        Stream.of(values).forEach(System.out::print);
        System.out.println();
        Arrays.sort(Arrays.stream(values).mapToInt(Integer::intValue).toArray());
        int i = 0;
        while ((i & 1) <= 0) {
            i++;
        }
        System.out.println(i);
    }
}
