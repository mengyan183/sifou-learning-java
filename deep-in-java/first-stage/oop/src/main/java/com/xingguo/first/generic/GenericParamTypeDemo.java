/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.generic;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * GenericParamTypeDemo
 * 泛型类型
 *
 * @author guoxing
 * @date 9/13/20 1:59 PM
 * @since
 */
public class GenericParamTypeDemo {

    public static void main(String[] args) {
        // 这里限定了当前泛型为String
        Container<String> stringContainer = new Container<>("1");
        // 对于后面实例化对象时由于未指定泛型的具体类型,因此在操作时实际限制为Object类型
        // 因此运行时也不会报错;(当未限定泛型上限时)
        Container<Integer> integerContainer = new Container("1");
        // 但在运行时就会抛出错误,对于integerContainer实际存储的类型为String类型,而get方法返回的是Integer类型
        // 当进行强制类型转换时,会抛出类型转换错误
        try {
            Integer element = null;
            Object o = integerContainer.getElement();// 这里的报错是由于将返回值String类型赋值给Integer类型时发生的错误
            System.out.println(o.getClass().getTypeName());
            // 对于 Integer element = integerContainer.getElement(); 实际是将一个实际类型为String的Object类型数据强制转换为Integer类型;因此会抛出类型转换异常
        } catch (ClassCastException c) {
            System.out.println(c.getMessage());
        }

        // 当限定了类型的单一上限为 CharSequence 时
        // 由于String 和 StringBuffer 都是 CharSequence的子级,因此编译时不会报错
        Container<StringBuffer> stringBufferContainer = new Container("1");
        // 为什么这里可以正常输出?
        // 原因为: 由于运行时类型擦除, 对于stringBufferContainer中实际都是Object类型,但隐藏的类型为String
        // 当在调用get方法使用时,实际进行了一下步骤 ((Object)((String)("1"))).toString()
        System.out.println(stringBufferContainer.getElement()); // 通过debug我们可以看到这里实际是调用的String中的toString方法, 虽然返回的结果为Object,但其实际类型为String,体现了多态

        add(new ArrayList<>(), 1);
        add(new ArrayList<>(), "string");

        castQuestion();

    }

    public static void castQuestion() {
        // 在执行实例化操作时,实际已经隐式限定了当前对象的类型
        // 在执行具体操作时,虽然根据变量的限定符显式定义,但在实际使用中就会抛出错误
        Container<StringBuilder> stringContainer = new Container("1");
        StringBuilder element = stringContainer.getElement();
        Container<Integer> integerContainer = new Container("1");
        Integer integerContainerElement = integerContainer.getElement();

    }

    // 自定义内部容器类,类型为泛型
    // 单界限操作: E extends CharSequence,这里限定了泛型的类型只能为CharSequence的子级
    public static class Container<E extends Serializable> {
        private E element;

        public Container(E element) {
            this.element = element;
            // 可以看到当前元素的实际类型
            System.out.println(element.getClass().getTypeName());
        }

        // 方法
        public E getElement() {
            return element;
        }

        public void setElement(E element) {
            this.element = element;
        }
    }

    // 多界限泛型
    class A {

    }

    interface B {

    }

    interface C {

    }

    // 对于泛型多界限限制要求第一个限制类型可以为类/接口,之后的所有限制类型都只能为接口
//    class D<T extends A & B & C> {
    class D<T extends Serializable & B & C> {// 第一个限制类型为接口,也允许编译通过
//    class D<T extends B & A & C> { // 此时会抛出编译错误,提示当前A所在位置必须为interface类型

    }


    // 泛型方法
//    public static void add(Collection<E> collection,E element){// 对于当前类上没有对E当前这个泛型参数进行定义,因此不能直接使用,需要在返回参数类型之前进行定义
//    public static <E> void add(Collection<E> collection, E element) {
    public static <C extends Collection<E>, E extends Serializable> void add(C collection, E element) {
        collection.add(element);
    }
}
