/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.reflection.application;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

/**
 * ArrayDemo
 * 数组类型数据
 *
 * @author guoxing
 * @date 2020/11/20 9:15 AM
 * @since
 */
@Slf4j
public class ArrayDemo {

    public static void main(String[] args) {

        /**
         *     0: iconst_1
         *          1: newarray       int
         *          3: astore_1
         *          可以看到对于  " new int[1]" 这个操作主要是通过 newarray 指令实现
         */
        // 对于 数组 作为特殊的java 原生对象,其具体实现实际是由 jvm 指令代替
        int[] iArray = new int[1];

        Class<? extends int[]> aClass = iArray.getClass();
        // 判断当前类型是否为数组类型
        boolean array = aClass.isArray();
        log.info("当前类{}是否为数组类型:{}", aClass, array ? "是" : "否");

        // 获取当前类的继承类
        Type genericSuperclass = aClass.getGenericSuperclass();
        log.info("{}", genericSuperclass.getTypeName());
        // 获取实现的接口
        Type[] genericInterfaces = aClass.getGenericInterfaces();
        Stream.of(genericInterfaces)
                .forEach(type -> {
                    // 泛型类型
                    if (type instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) type;
                        Type rawType = parameterizedType.getRawType();
                        log.info("{}", rawType);
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        Stream.of(actualTypeArguments)
                                .forEach(t -> {
                                    log.info("{}", t);
                                });
                    } else {
                        // 普通类型
                        log.info("{}", type);
                    }
                });

        //获取当前数组的元素类型
        Class<?> componentType = aClass.getComponentType();
        log.info("{},{}", componentType, componentType.isPrimitive());

    }
}
