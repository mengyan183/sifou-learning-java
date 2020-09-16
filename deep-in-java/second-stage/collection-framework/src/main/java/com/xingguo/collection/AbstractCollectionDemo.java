/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

/**
 * AbstractCollectionDemo
 *
 * @author guoxing
 * @date 9/16/20 2:27 PM
 * @since
 */
public class AbstractCollectionDemo {
    public static void main(String[] args) {
        abstractList();
    }

    private static void abstractList() {
        // 这里返回的实际是 Arrays.ArrayList extends AbstractList
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);

        assert integers instanceof AbstractList;
        // 对于Arrays.ArrayList实际并没有实现AbstractList中的add操作,因此当执行add方法时实际是调用的AbstractList#add
        try {
            integers.add(1);
        } catch (UnsupportedOperationException exception) {
           exception.printStackTrace();
        }
        // Arrays.ArrayList 支持修改操作
        integers.set(0, 0);
    }
}
