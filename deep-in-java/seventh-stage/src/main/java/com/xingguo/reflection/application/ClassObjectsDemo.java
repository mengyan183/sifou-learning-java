/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.reflection.application;

import java.beans.ConstructorProperties;
import java.lang.reflect.Modifier;
import java.time.format.TextStyle;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

/**
 * ClassObjectsDemo
 * java 中 不是所有的类型都是对象,但所有的类型都有类对象(Class Objects)
 *
 * @author guoxing
 * @date 2020/11/12 5:46 PM
 * @since
 */
public class ClassObjectsDemo {
    /**
     * jvm 启动命令 增加 -ea 开启 assert
     *
     * @param args
     */
    public static void main(String[] args) {
        /**
         * 对于 Class 实际区分 具体实现类和抽象类, 可以通过modifier 修饰符来判断, 对于jdk 并未提供实现类判断接口,因此只能通过是否为抽象类判断
         */
        // 具体 类
        Class<Object> classClass = Object.class;
        assert !Modifier.isAbstract(classClass.getModifiers());
        // 抽象类
        assert Modifier.isAbstract(AbstractList.class.getModifiers());
        // 接口
        Class<List> listClass = List.class;
        assert listClass.isInterface();
        // 枚举
        Class<TextStyle> textStyleClass = TextStyle.class;
        assert textStyleClass.isEnum();
        // 注解
        Class<ConstructorProperties> constructorPropertiesClass = ConstructorProperties.class;
        assert constructorPropertiesClass.isAnnotation();
        // 原生类型
        Class<Integer> integerClass = int.class;
        assert integerClass.isPrimitive();
        // 数组
        assert int[].class.isArray();
        // 特殊类型
        Class<Void> voidClass = void.class;

    }
}
