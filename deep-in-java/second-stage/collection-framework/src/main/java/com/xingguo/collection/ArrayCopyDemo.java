/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection;

/**
 * ArrayCopyDemo
 *
 * @author guoxing
 * @date 9/15/20 5:27 PM
 * @since
 */
public class ArrayCopyDemo {
    public static void main(String[] args) {
        String[] strings = new String[1];
        // 对于 arraycopy中的参数为Object分析,原因在于 对于数组有可能存在多维数组,为了保证数据的通用性
//        System.arraycopy();
//        System.arraycopy(new String[1], 0, new String[1][1], 0, 1); // 如果原数组中不存在数据时,不会抛出错误
//        System.arraycopy(new String[]{"1"}, 0, new String[1][1], 0, 1); // 如果原数组中存在数据,在复制时会提示java.lang.ArrayStoreException: arraycopy: type mismatch: can not copy java.lang.String[] into [Ljava.lang.String;[]
//        System.out.println(strings1);
        arraycopy(strings, strings);

        // 数字越位
        int maxValue = Integer.MAX_VALUE;
        System.out.println(maxValue + 1 == Integer.MIN_VALUE);
        //在java 中 int 为 4个字节(32位), 因此 对于 int 的最大数据为 2 ^ 32 - 1
        // 1 byte = 4 bit

    }

    public static void arraycopy(Object src, Object dest) {

    }
}
