/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * StringDemo
 *
 * @author guoxing
 * @date 9/11/20 10:24 AM
 * @since
 */
public class StringDemo {
    public static void main(String[] args) throws Exception {
        // 当前对象a属于常量 : 是语言特性; 实际是对象类型常量化
        String a = "hello"; // String作为一种特殊的对象,支持通过字面常量直接赋值;
        // 面向对象的规则: 一切对象都需要new
        String b = new String("hello"); // 作为对象类型,同样也支持使用new 关键字;

        String value2 = "hello";

        // 虽然String对象无法直接改变, 但支持可以通过反射的方式来修改对象中属性数据
        byte[] afterValueArr = "world".getBytes();
        // 通过反射获取私有 value字段
        Field declaredValueField = String.class.getDeclaredField("value");
        // 设置当前字段是可访问的
        declaredValueField.setAccessible(true);
        // 将当前value2对象中的value字段修改为 指定的数据
        declaredValueField.set(value2, afterValueArr);
        System.out.println(value2);
    }

    // 对于 String 类型我们了解 其是不可变的
    // 对于String 为什么要设置为不可变的
    // 如果String设置为可继承,则表示当前类中所有非final修饰的非private的方法都可以被继承和重写,因此对于不想变的公共方法只能全部都添加final修饰
    class ExtendableString {
        private final byte[] value;
        private int hash;
        private boolean hashIsZero;

        ExtendableString(byte[] value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object anObject) {
            return true;
        }

        @Override
        public int hashCode() {
            int h = hash;
            return h;
        }
    }
}
