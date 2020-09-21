/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.algorithms;

import java.util.stream.Stream;

/**
 * InsertSort
 * 插入排序
 *
 * @author guoxing
 * @date 9/19/20 4:45 PM
 * @since
 */
public class InsertSort<T extends Comparable<T>> implements Sort<T> {

    /**
     * 插入排序
     * 例如:
     * 5   4   3   2   1
     * j   i
     * j 和 j+1 位进行比较
     * 5 > 4 ,交换 5 和 4 的位置,且 j 指向的索引为 0,比较中止,i的位置往后移动一位, j 的位置 为 i-1
     * 4   5   3   2   1
     *      j   i
     * 5 > 3,交换 5 和 3 的位置,但j指向的索引不为0.因此,继续比较,i的位置不变,j的位置往前移动一位
     * 4   3   5   2   1
     * j       i
     * 4 > 3,交换 3 和 4 的位置,且j指向的索引为 0 ,比较中止,i的位置往后移动一位,j的位置为 i - 1
     * ..............
     *
     * @param array
     * @param flag  true 代表的是正序;false代表的是逆序
     */
    @Override
    public void sort(T[] array, boolean flag) {
        if (array == null || array.length < 2) {
            return;
        }
        int length = array.length;
        for (int i = 1; i < length; i++) {
            // 对于compareValue一直等于 array[j+1]
            T compareValue = array[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                // flag && array[j].compareTo(compareValue) == 1 (array[j] > compareValue) 升序
                // !flag && array[j].compareTo(compareValue) == -1 (array[j] < compareValue) 降序
                if ((flag && array[j].compareTo(compareValue) == 1) || (!flag && array[j].compareTo(compareValue) == -1)) {
                    array[j + 1] = array[j];
//                    array[j] = compareValue;// 该交换操作实际是没有必要的
                } else {
                    break;
                }
            }
            // 可以在全部比较操作都执行结束后,在结束游标的后一位将当前比较的元素塞入数组中
            array[j + 1] = compareValue;
        }
    }

    public static void main(String[] args) {
        Integer[] values = new Integer[]{5, 3, 2, 1};
        Sort<Integer> integerInsertSort = new InsertSort<>();
        integerInsertSort.sort(values, true);
        Stream.of(values).forEach(System.out::print);
        System.out.println();
        integerInsertSort.sort(values, false);
        Stream.of(values).forEach(System.out::print);
    }
}
