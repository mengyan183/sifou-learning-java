/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first;

import java.io.Serializable;

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

    }

    /**
     * 内部类 方法重载(参数为相同类的不同接口实现)
     */

    static class InnerClass {
        public void printLn(A a) {
            System.out.println("A");
        }

        public void printLn(B b) {
            System.out.println("B");
        }

        public void stringPrintLn(CharSequence charSequence){
            System.out.println("charSequence");
        }
        public void stringPrintLn(Serializable serializable){
            System.out.println("serializable");
        }

    }

    interface A {

    }

    interface B {

    }

   static class C implements A, B {

    }

}
