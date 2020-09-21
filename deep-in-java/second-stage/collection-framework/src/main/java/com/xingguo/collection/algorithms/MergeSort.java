/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.algorithms;

import java.util.stream.Stream;

/**
 * MergeSortDemo
 * 合并排序
 * 主要包含分区和合并两个操作,首先进行分区操作,当分区到最小单位时,进行合并操作
 *
 * @author guoxing
 * @date 2020/9/20 12:39 PM
 * @since
 */
public class MergeSort<T extends Comparable<T>> implements Sort<T> {

    /**
     * @param array
     * @param flag  true 代表的是正序;false代表的是逆序
     */
    @Override
    public void sort(T[] array, boolean flag) {
        partition(array, 0, array.length - 1, flag);
    }

    /**
     * 分区操作,通过求中间值进行分区
     *
     * @author guoxing
     * @date 2020-09-20 12:42 PM
     * @since
     */
    public void partition(T[] array, int low, int high, boolean flag) {
        if (low >= high) {
            return;
        }
        int mid = low / 2 + high / 2; // 为了避免 low + high 超过int的范围,因此先执行除法再执行加法
        partition(array, low, mid, flag); // 左区间 包含中间值
        partition(array, mid + 1, high, flag);// 有区间 不包含中间值
        // 对分区进行合并
        merge(array, low, mid, high, flag);
    }

    private void merge(T[] array, int low, int mid, int high, boolean flag) {
        // 将分区的区间正式分离
        // 由于泛型T不能直接创建对象,因此使用其父类
        int leftLength = mid - low + 1;
        // 左区间 [low,mid]
        Comparable[] leftPart = new Comparable[leftLength];
        System.arraycopy(array, low, leftPart, 0, leftLength);
        int rightLength = high - mid;
        // 右区间 (mid,high]
        Comparable[] rightPart = new Comparable[rightLength];
        System.arraycopy(array, mid + 1, rightPart, 0, rightLength);

        // 对比左右两个区间进行合并操作
        // 要操作的原数组的索引起始位置
        int index = low;
        // 左右区间遍历起始索引
        int leftIndex = 0, rightIndex = 0;
        /**
         * 在对比两个区间时,只要有一个区间遍历完毕,则表示另一个区间中的剩余的数据肯定是全部都大于或小于已遍历过的元素
         * 例如 : [1,2,3] [2,4,5]
         * [1,2,2,3] 当第一个区间遍历结束后,第二个区间剩余[4,5] 第二个区间剩余的元素全部都大于已遍历的元素
         *
         *
         */
        for (; leftIndex < leftLength && rightIndex < rightLength;) {
            Comparable leftData = leftPart[leftIndex];
            Comparable rightData = rightPart[rightIndex];
            if ((flag && leftData.compareTo(rightData) < 1) || (!flag && leftData.compareTo(rightData) > -1)) {
                // 将左区间的数据放入到原数组中
                array[index++] = (T) leftData;
                leftIndex++;
            } else {
                // 将右区间的数据放入到原数组中
                array[index++] = (T) rightData;
                rightIndex++;
            }
        }
        // 判断左区间是否存在剩余数据
        while (leftIndex < leftLength) {
            // 将剩余数据写入到原数组中
            array[index++] = (T) leftPart[leftIndex++];
        }
        // 判断右区间是否存在剩余数据
        while (rightIndex < rightLength) {
            // 将剩余数据写入到原数组中
            array[index++] = (T) rightPart[rightIndex++];
        }
    }

    public static void main(String[] args) {
        Integer[] values = new Integer[]{5, 3, 2, 1};
        Sort<Integer> integerInsertSort = new MergeSort<>();
        integerInsertSort.sort(values, true);
        Stream.of(values).forEach(System.out::print);
        System.out.println();
        integerInsertSort.sort(values, false);
        Stream.of(values).forEach(System.out::print);
    }
}
