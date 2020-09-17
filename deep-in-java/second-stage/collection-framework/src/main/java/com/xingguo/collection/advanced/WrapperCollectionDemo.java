/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.advanced;

import java.util.*;

/**
 * WrapperCollectionDemo
 * 集合包装类型
 *
 * @author guoxing
 * @date 9/17/20 10:17 AM
 * @since
 */
public class WrapperCollectionDemo {
    public static void main(String[] args) {
        CheckedCollection checkedCollection = new CheckedCollection();
        checkedCollection.collectionGenericType();

    }
}

class SynchronizedCollectionDemo {
    /**
     * 集合包装同步类型对比vector
     * 虽然两者都是线程安全的,但由于其使用Synchronized 同步锁的范围不同
     * 对于Vector 而言其Synchronized是直接加载方法上的,意味着锁的范围更大,对于当前进程内所有使用Vector类的代码当执行操作时都会被阻塞
     * 但对于Synchronized集合是通过锁当前对象来保证的同步性
     * 因此相对而言,Vector的锁的范围更广,所以产生死锁的概率更高
     */
    public void synchronizedRandomListVsVector() {
        ArrayList<Integer> integers = new ArrayList<>();
        List<Integer> synchronizedList = Collections.synchronizedList(integers);
        Vector<Integer> vector = new Vector<>(integers);
    }
}

class CheckedCollection {

    public void collectionGenericType() {
        List<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        // 由于泛型存在编译时校验,运行时擦写
        List noGenericTypeList = integers;
        System.out.println(noGenericTypeList == integers);
        // 虽然 noGenericTypeList 引用了 integers
        // 运行时泛型擦写  List<Integer> -> List<Object> -> List
        // 因此可以写入任意类型的数据
        noGenericTypeList.add("A");
        // 由于数据读取时需要进行类型转换(转换为泛型的指定类型)因此会抛出ClassCastException
//        integers.forEach(System.out::println);
        // 而对于noGenericTypeList由于没有泛型的约束,因此读取数据是都是按照Object类型处理
        noGenericTypeList.forEach(System.out::println);
        // 在转换时并没有执行类型检查因此支持直接转换
        List<Integer> castList = new ArrayList<>(noGenericTypeList);
        // 因此为了避免类型擦写导致的异常,因此需要使用包装类型工具类
        // 当转换为checkedList时并不会进行类型校验
        /**
         * Wrapper(装饰器)模式的使用
         * Collections.checked*接口弥补了 泛型运行时擦写的不足
         * 强类型: 编译时泛型强制类型检查,运行时利用Collections.checked*强类型检查
         */
        List<Integer> checkedList = Collections.checkedList(castList, Integer.TYPE);
        // 会生成新的数据
        System.out.println(checkedList == castList);

        noGenericTypeList = checkedList;
        // 对于checkedList在执行添加时,会执行类型校验,因此会直接抛出错误
        noGenericTypeList.add("B");
    }
}
