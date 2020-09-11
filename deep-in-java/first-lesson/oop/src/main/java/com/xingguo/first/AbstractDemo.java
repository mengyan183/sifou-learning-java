/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first;

import java.io.Serializable;

/**
 * AbstractDemo
 *
 * @author guoxing
 * @date 9/11/20 11:37 AM
 * @since
 */
public class AbstractDemo {

    interface B {
        void printLn(Serializable s);

        default void printLn(CharSequence charSequence) {
            System.out.println(charSequence);
        }
    }

    public abstract static class AbstractClass implements B {
        // 对于属性的声明,接口只能定义常量,并不支持属性的定义
        private String s;

        // 对于可以实现方法这一特点,接口是可以直接替代抽象类的
        @Override
        public void printLn(Serializable s) {
            System.out.println(s);
        }
    }
}
