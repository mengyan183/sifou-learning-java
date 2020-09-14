/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional;

/**
 * ActionDemo
 * 隐藏类型;单纯的执行
 *
 * @author guoxing
 * @date 9/14/20 11:49 AM
 * @since
 */
public class ActionDemo {

    public static void main(String[] args) {
        Runnable runnable = () -> System.out.println("1");
    }
}
