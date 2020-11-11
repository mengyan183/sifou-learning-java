/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.reflection;

import javax.print.attribute.UnmodifiableSetException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * ClassModifiers
 *
 * @author guoxing
 * @date 2020/11/11 12:32 PM
 * @since
 */
public class ClassModifiers<E> implements Serializable {
    // 对于访问限定符 public / protected / default / private 支持修饰 Type/Method/Param
    protected String data;
    private Integer value;
    // 使用static 修饰表示其归属于Class
    // 使用final 表示其不可变性(针对前端编译阶段以及编写)
    public static final Object CONSTANT_DATA = new Object();
    // 使用 volatile 表示其可见性,并在后端编译阶段以及运行阶段 会使用 cycleBarrier 保证了 synchronize-relation 或 happens-before
    private volatile Integer shareData;
    // 对于transient 主要是 应用于序列化操作, 对于使用transient修饰的字段 在序列化阶段并不会将当前字段的数据转换为字节流,因此在反序列化时得到的结果就是当前字段的默认值
    // 特别是针对RPC操作而言
    private transient int hash;


    // 方法修饰符

    // 表示当前方法为一个native 方法,其具体的实现在本地依赖对应的 c/cpp 代码中
    public native int hash();

    // 对于 static 修饰的 方法也表明了当前方法归属于当前类
    public static void main(String[] args) {

    }

    // 对于使用 final 修饰表示其不可变性
    public final List<E> getUnmodifiableList(List<E> list) {
        return Collections.unmodifiableList(list);
    }

    // 使用 synchronized 修饰表示其操作的同步性
    public synchronized String getData() {
        return this.data;
    }

    // strict float point
    // 使用 strictfp 修饰方法或类
    //  strictfp只是保证浮点运算都严格按照IEEE754进行，保证了各种平台下浮点运算规则的统一，并不是确保了高精度
    // 如果为了保证高精度 需要使用 java.math.BigDecimal
    public strictfp float test() {
        return 0.0f;
    }

}

abstract class AbstractClass {
    // 对于 显式 abstract method 只能属于 abstract class
    public abstract void getName();

    // 而对于 abstract class 既可以包含抽象方法也可以存在具体实现方法
    public void print() {

    }
}
// default 修饰类 表示只能同包下访问
class DefaultClass extends ClassModifiers {
    public String getSuperData() {
//        super.value;
        return super.data;
    }
}