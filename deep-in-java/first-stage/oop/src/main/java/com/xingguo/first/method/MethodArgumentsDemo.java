/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.method;

import java.util.Collection;

/**
 * MethodArgumentsDemo
 * 方法参数设计
 *
 * @author guoxing
 * @date 9/13/20 9:53 PM
 * @since
 */
public class MethodArgumentsDemo {

    public static void main(String[] args) {

    }

    // 方法参数名称要求简明扼要
    // 对于参数类型可以对等也可非对等
    // 扩展性
    public void add(Collection<Object> collection, Object element) {

    }

    public void add(Collection<Object> collection, Object... elements) {

    }

    public void add(Collection<Object> collection, Object element, Object... elements) {

    }

    // 对于Effective java 规范要求 参数最多不能超过四个
    // 在java8 lambda中 则要求参数数量不能超过三个
}
