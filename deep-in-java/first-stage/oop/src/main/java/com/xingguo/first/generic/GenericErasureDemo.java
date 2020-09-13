/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * GenericErasureDemo
 * 泛型类型擦除
 *
 * @author guoxing
 * @date 9/13/20 8:09 PM
 * @since
 */
public class GenericErasureDemo {

    public static void main(String[] args) {
        /**
         * 编译后的代码
         *List<String> values = Arrays.asList("1");
         *String s = (String)values.get(0);
         */
        List<String> values = Arrays.asList("1");
        String s = values.get(0);

        List<String> objectList = Collections.emptyList();

        A a = new A();
        a.compareTo(a);
    }

    // 通过泛型类型的传递保留了多态性
    public static class A implements Comparable<A> {
        @Override
        public int compareTo(A o) {
            return 0;
        }
    }
}
