/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.algorithms;

/**
 * Sort
 * 排序接口
 * TODO 学习java中算法实现
 *
 * @author guoxing
 * @date 9/19/20 11:03 AM
 * @since
 */
public interface Sort<T extends Comparable<T>> {
    /**
     * 对数组进行排序
     *
     * @param array
     * @param flag  true 代表的是正序;false代表的是逆序
     */
    void sort(T[] array, boolean flag);
}
