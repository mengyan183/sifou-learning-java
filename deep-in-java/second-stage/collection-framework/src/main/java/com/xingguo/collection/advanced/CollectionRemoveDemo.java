/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.advanced;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * CollectionRemoveDemo
 *
 * @author guoxing
 * @date 9/17/20 5:26 PM
 * @since
 */
public class CollectionRemoveDemo {
    public static void main(String[] args) {
        collectionRemove();
    }

    public static void collectionRemove(){
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        // 常规删除
        //         list.removeIf(next -> next == 3);
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
//            Integer next = iterator.next();
            // 当不执行 next时会抛出 异常,原因在于,不允许删除未读的数据,只能删除游标之前的数据
            iterator.remove();

        }
        list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        // 这种方式可以删除
        for (int i = 0; i < list.size(); i++) {
            Object o = (Object) list.get(0);
            list.remove(o);
        }
        list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        // 这种方式会抛出数组越界异常
        /**
         * 原因在于remove作为重载方法,当数据为数字类型时,是按照索引进行删除
         */
        for (int i = 0; i < list.size(); i++) {
            Integer integer = list.get(i);
            list.remove(integer.intValue());
        }
        list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        // 错误方式
        list.forEach(list::remove);
    }
}
