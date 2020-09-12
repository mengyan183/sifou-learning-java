/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first;

import java.io.Serializable;

/**
 * IntefaceDemo
 *
 * @author guoxing
 * @date 9/11/20 11:37 AM
 * @since
 */
public class InterfaceDemo {


    public interface A {
        // 对于接口中定义的数据都是常量
        int CONSTANT_VALUE = 1;

        //
        int commonFunc();

        // 对于java8+ 接口中也支持默认方法实现
        default int getConstantValue() {
            return CONSTANT_VALUE;
        }

        void printLn(CharSequence charSequence);

        void printLn(Serializable serializable);

    }

    // 抽象类也是支持再次扩展的
    public abstract static class AbstractA implements A {
        private String name;

        // 对于抽象类可以实现接口中的方法,提供通用实现,并继承接口中得到常量
        @Override
        public int commonFunc() {
            return CONSTANT_VALUE;
        }
    }

    public static class AImpl extends AbstractA {
        @Override
        public void printLn(CharSequence charSequence) {
            System.out.println("charSequence:" + charSequence);
        }

        @Override
        public void printLn(Serializable serializable) {
            System.out.println("serializable:" + serializable);
        }


    }

    static class CustomString implements CharSequence, Serializable {
        private final String value;

        public CustomString(String value) {
            this.value = value;
        }


        @Override
        public int length() {
            return 0;
        }

        @Override
        public char charAt(int index) {
            return 0;
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return null;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static void main(String[] args) {
        AImpl a = new AImpl();
        int i = a.commonFunc();
        System.out.println(i);
        String v = "1";
        // 对于重载方法虽然参数类型是不同的,但对于接口类型的参数,如果一个实现类同时实现了多个接口,则要求在调用重载方法时必须进行强制类型转换来指定要使用的具体方法
        a.printLn((Serializable) v); // 打印结果都是 serializable:1 ; 且不需要使用类型强制转换
        CustomString customString = new CustomString("1");
        a.printLn((CharSequence) customString);

        printLn((B) new MultiInterface());
    }

    interface B {

    }

    interface C {

    }

    static class MultiInterface implements B, C {

    }

    private static void printLn(B b) {
        System.out.println("B:" + b);
    }

    private static void printLn(C c) {
        System.out.println("C:" + c);
    }
}
