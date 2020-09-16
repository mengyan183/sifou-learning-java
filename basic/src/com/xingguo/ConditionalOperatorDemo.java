/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo;

/**
 * ConditionalOperatorDemo
 *
 * @author guoxing
 * @date 9/16/20 8:59 PM
 * @since
 */
public class ConditionalOperatorDemo {
    public static void main(String[] args) {
        rightConditionalOperator();
        errorConditionalOperator();
    }

    public static void rightConditionalOperator() {
        Integer i = 0 == 0 ? 1 : null;
        System.out.println(i);
    }

    public static void errorConditionalOperator() {
        Integer j = null;
        /**
         * 使用javap -v 查看源码之后会发现 存在以下代码
         * invokevirtual #33                 // Method java/lang/Integer.intValue:()
         * 由于 1 默认为 int 类型,而三目的条件结果为false,会执行到 ": j"这里,由于j为Integer类型 ,可以自动拆箱转换为int类型,因此就抛出了NPE
         * 对于自动拆箱还隐藏的一点在于 只有 ":"左侧为基本数据类型,而右侧为基本数据类型的包装类型时才会执行自动拆箱操作
         */
//        System.out.println(0 != 0 ? 1 : j);
        // 和下面的代码不同点,对于 null 在编译后实际认为是Object类型
//        System.out.println(0 != 0 ? 1 : null);
        Object o = false ? (Long) null : 1;
//        Object o = true ? (Long) null : 1;
        System.out.println(true ? (Long) null : 1);
        System.out.println(false ? 1 : (Long) null);
        System.out.println(false ? Long.valueOf(1L) : (Long) null);
        System.out.println(false ? 1 : (String) null);
    }
}
