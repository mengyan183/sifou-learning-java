/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ClassCastDemo
 *
 * @author guoxing
 * @date 9/13/20 1:05 PM
 * @since
 */
public class ClassCastDemo {

    public static void main(String[] args) {
        // 如果未声明泛型的具体类型,则默认为Object类型
        List list = new ArrayList();
        list.add(1);
        list.add("string");
        list.add(true);

        List<Object> objectList = new ArrayList<>();
        // 这里实际通过泛型类型约束实现了类型强转
        List<String> stringList = Collections.emptyList();
        // 对于泛型未指定的进行赋值,编译时期不会报错,但在运行时则有可能报错
        stringList = list;
        // 在使用时进行强制类型转换会抛出异常
//        stringList.forEach(System.out::println);
        // 由于指定了泛型,在强类型的约束下,并不能直接进行赋值操作
//        stringList = objectList;
        // 对于当前返回的实际是自定义的ArrayList,并没有实现相关的写入操作,因此在调用写入操作时会抛出异常信息
        List<String> stringList1 = Arrays.asList("1", "2");
        // 该代码实际会调用java.util.AbstractList.add(int, E) 抛出异常
//        stringList1.add("2");
        // 对于不同的java版本实际提供了不同的语法特性
        var varList = new ArrayList<String>();// 这里实际就是实现了自动类型识别
        var i = 1;// 这里i就会自动进行类型识别为int
    }

    /**
     * 当为设置泛型时,实际并不清楚集合中存储的元素具体类型,只能通过doc来约束控制
     * <p>
     * 将源数据转换为目标数据
     *
     * @param dest   源数据
     * @param target 目标数据
     * @author guoxing
     * @date 2020-09-13 1:07 PM
     * @since
     */
    public static void transferList(List dest, List target) {
        dest.addAll(target);//由于未指定泛型,默认都为Object,因此在编译时不会抛出错误
    }
}
