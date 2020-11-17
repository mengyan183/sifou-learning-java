/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.reflection.application;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * ClassMembersDemo
 * java类成员
 *
 * @author guoxing
 * @date 2020/11/16 8:12 PM
 * @since
 */
public class ClassMembersDemo {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        /**
         * 获取类中的成员,主要来自于java.lang.Class
         * 对于 getDeclaredXXX
         */

        // getDeclaredFields 只会获取当前类中所有的字段,并不会获取其存在继承关系的类数据
        // 对于内置类还存在一个特殊的字段
        Class<ExtendedData> extendedDataClass = ExtendedData.class;
        Field[] declaredFields = extendedDataClass.getDeclaredFields();
        Stream.of(declaredFields)
//                .map(Field::getType)
                .forEach(System.out::println);
        System.out.println("=================");
        List<Field> fields = listAllFields(extendedDataClass);
        fields.stream().map(Field::getName).forEach(System.out::println);

        System.out.println("=================");
        Method[] declaredMethods = extendedDataClass.getDeclaredMethods();
        Stream.of(declaredMethods).forEach(System.out::println);

        System.out.println("=================");
        // 对于类中的成员实际是存在访问性限制, 即使通过反射也不能直接访问 受限的成员
        // 例如访问 Data#setData ,当前方法为private
        /**
         * Exception in thread "main" java.lang.IllegalAccessException: Class com.xingguo.reflection.application.ClassMembersDemo can not access a member of class com.xingguo.reflection.application.ClassMembersDemo$Data with modifiers "private"
         * 当执行该私有方法时会抛出无权限访问的异常
         */
        Class<Data> dataClass = Data.class;
        Method setData = dataClass.getDeclaredMethod("setData", int.class);
        // 对于无权限访问的成员,需要通过修改 accessible来实现配置
        setData.setAccessible(true);
        Data data = dataClass.newInstance();
        setData.invoke(data,1);

    }

    /**
     * 递归获取所有具有继承关系的类直到Object中止
     *
     * @param klass
     * @return
     */
    public static List<Field> listAllFields(Class<?> klass) {
        // 中止条件
        if (klass == null || klass.equals(Object.class)) {
            return Collections.emptyList();
        }
        Field[] declaredFields = klass.getDeclaredFields();
        ArrayList<Field> fields = new ArrayList<>(Arrays.asList(declaredFields));
        Class<?> superclass = klass.getSuperclass();
        fields.addAll(listAllFields(superclass));
        return fields;
    }

    private String classMembersDemoName;

    static class Data {
        private int data;

        public int getData() {
            return data;
        }

        private void setData(int data) {
            this.data = data;
        }
    }

    class ExtendedData extends Data {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void printClassMembers() {
            System.out.println(classMembersDemoName);
        }

    }
}
