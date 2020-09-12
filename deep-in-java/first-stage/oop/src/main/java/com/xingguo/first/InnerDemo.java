/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * InnerDemo
 *
 * @author guoxing
 * @date 9/11/20 2:54 PM
 * @since
 */
public class InnerDemo {
    public static void main(String[] args) {
        InnerClass innerClass = new InnerClass();
        C c = new C();
        innerClass.printLn((A) c);
        innerClass.stringPrintLn((Serializable) "1");
        // 内置类 java.lang.ThreadLocal.ThreadLocalMap,作为数据存储
        ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
        stringThreadLocal.set("1");
        // java.util.Collections.UnmodifiableCollection 只允许查询不允许写入
        List<String> strings = Collections.unmodifiableList(new ArrayList<>(Arrays.asList("1", "2", "3")));
        System.out.println(strings.get(0));
//        strings.add("1"); // 该操作会直接抛出异常
        Stream.Builder<Object> builder = Stream.builder();
    }

    /**
     * 内部类 方法重载(参数为相同类的不同接口实现)
     */
    // 对于需要暴露对外的内部类 则 需要显式使用public static 进行修饰
    public static class InnerClass {
        public void printLn(A a) {
            System.out.println("A");
        }

        public void printLn(B b) {
            System.out.println("B");
        }

        public void stringPrintLn(CharSequence charSequence) {
            System.out.println("charSequence");
        }

        public void stringPrintLn(Serializable serializable) {
            System.out.println("serializable");
        }

    }

    // 对于内置接口 当需要暴露对外时,不需要显式声明一个static
    public /* static */ interface A {

    }

    public interface B {

    }

    public static class C implements A, B {

    }

}
