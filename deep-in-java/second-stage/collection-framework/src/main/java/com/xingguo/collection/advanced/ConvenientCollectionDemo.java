/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.advanced;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * ConvenientCollectionDemo
 * 便利集合接口实现
 *
 * @author guoxing
 * @date 9/16/20 5:24 PM
 * @since
 */
public class ConvenientCollectionDemo {
    public static void main(String[] args) {
        SingleTonCollectionDemo singleTonCollectionDemo = new SingleTonCollectionDemo();
        Integer[] integerArray = singleTonCollectionDemo.getIntegerArray(1, 2, 3, 4);
        Stream.of(integerArray).forEach(System.out::println);
        integerArray[0] = 0;
        /**
         * 此处打印的结果为 0,2,3,4
         * 原因在于 虽然数组是引用类型,数组中的元素也是引用类型;但对于一些支持字面量操作的对象(引用)类型,实际都无法修改原有对象中存储的值,只能创建新的对象
         * 因此对于结果而言实际是将数组中的元素进行了替换,而非修改数组中的原始元素
         */
        Stream.of(integerArray).forEach(System.out::println);


        // =====================

        SingleTonCollectionDemo.User[] userArray = singleTonCollectionDemo.getUserArray(1, 2, 3, 4);
        Stream.of(userArray).forEach(System.out::println);
        userArray[0].setAge(0);
        /**
         * 此时输出的结果就发生了变化,原因在于是直接修改的数组元素内部的属性,而数组的元素并未发生变化
         * 虽然数组不可变,但数组中的元素的内部属性发生了变化,对于当前数组而言,实际还是发生了变化
         */
        Stream.of(userArray).forEach(System.out::println);
        // 使用复制操作镜像数据
        SingleTonCollectionDemo.User[] copyUserArray = Arrays.copyOf(userArray, userArray.length);
        copyUserArray[0].setAge(1);
        /**
         * 根据结果 1,2,3,4 可以看出Arrays.copy实际是浅拷贝
         */
        Stream.of(userArray).forEach(System.out::println);

    }
}

class SingleTonCollectionDemo { // 实际也可以称为 immutable

    /**
     * 数组存在长度不可变的特性
     *
     * @param values
     * @return
     */
    public Integer[] getIntegerArray(Integer... values) {
        int length = values.length;
        Integer[] integers = new Integer[length];
        for (int i = 0; i < length; i++) {
            integers[i] = values[i];
        }
        return integers;
    }

    public User[] getUserArray(int... values) {
        int length = values.length;
        User[] integers = new User[length];
        for (int i = 0; i < length; i++) {
            integers[i] = new User(values[i]);
        }
        return integers;
    }

    class User {
        private int age;

        public User(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "" + age;
        }
    }
}
