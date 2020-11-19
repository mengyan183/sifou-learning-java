/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.reflection.application;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * EnumDemo
 *
 * @author guoxing
 * @date 2020/11/19 8:57 PM
 * @since
 */
@Slf4j
public class EnumDemo {
    public static void main(String[] args) {
        // 反射 判断一个类 是否为 枚举类型 (修饰)
        Class<Color> colorClass = Color.class;
        log.info("{}", colorClass.isEnum());
        // 对于 enum 前端编译后得到的class 实际为 final class 枚举名 extends java.lang.Enum<当前类>
        boolean aFinal = Modifier.isFinal(colorClass.getModifiers());
        log.info("{}", aFinal);
        // 对于枚举也可以通过当前操作来判断,原因在于 对于继承 java.lang.Enum 就只能是enum类型不可能为其他类型
        log.info("{}", Enum.class.isAssignableFrom(colorClass));

        // 对于  java.lang.Class#getEnumConstants 和 java.lang.reflect.Field.isEnumConstant 只适用于 enum 类型
        log.info("{}", Arrays.stream(colorClass.getDeclaredFields()).anyMatch(Field::isEnumConstant));
        Arrays.stream(colorClass.getEnumConstants()).forEach(System.out::println);

        //TODO warning 以下为错误示范
        // 反射 判断类中的字段是否为枚举类型
        Class<EnumFiledObject> enumFiledObjectClass = EnumFiledObject.class;
        Field[] declaredFields = enumFiledObjectClass.getDeclaredFields();
        log.info("{}", Arrays.stream(declaredFields).anyMatch(Field::isEnumConstant));
        log.info("普通类中是否存在枚举{}", Arrays.stream(declaredFields).anyMatch(field -> field.getType().isEnum()));
        // 对于当前这个判断只适用于 枚举类
        EnumFiledObject[] enumConstants = enumFiledObjectClass.getEnumConstants();
        Optional.ofNullable(enumConstants)
                .ifPresent(constants -> Arrays.stream(constants).forEach(System.out::println));
    }
}

// 定义枚举
enum Color {
    Red,
    White;
}


class EnumFiledObject {
    public static final Color C = Color.Red;
    // 设置字段为枚举类型
    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
