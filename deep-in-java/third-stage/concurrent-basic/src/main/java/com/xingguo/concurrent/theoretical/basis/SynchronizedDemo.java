/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.theoretical.basis;

/**
 * SynchronizedDemo
 * 同步原语
 *
 * @author guoxing
 * @date 2020/9/24 1:10 PM
 * @since
 */
public class SynchronizedDemo {
    public static void main(String[] args) {
        synchronized (SynchronizedDemo.class) {
            setValue(1);
        }
    }

    public static void setValue(int value) {
        // 对于当前data变量而言,并不会存在多线程数据共享的问题
        // data的对象存在于堆区
        // 对于 data 变量名以及value变量名是存在于栈区的
        // 对于 栈区的数据是属于线程私有,虽然堆区的数据是共享的,但对于当前存在于堆区的 new Data 对象只有当前线程中的栈中的 data变量引用,因此并不存在共享的问题
        Data data = new Data();
        data.setValue(value);
    }

    public static class Data {
        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
