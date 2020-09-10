/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.function.base;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Callable;

/**
 * InnerClassDemo
 * 函数式基础 匿名内置类
 *
 * @author guoxing
 * @date 9/10/20 2:13 PM
 * @since
 */
public class InnerClassDemo {
    // 可以看到在 编译后的输出文件中存在 以下名称的class文件
    // InnerClassDemo$1 对于 InnerClassDemo是当前显式声明的类文件,对于$1表示的是当前类中的第一个匿名内部类
    // 对于数字的定义实际是按照匿名内部类的代码编写顺序进行定义的
    // 当需要调用匿名内部类时,匿名内部类的全路径为 当前显式声明的类的全路径.序号 例如: com.xingguo.function.base.InnerClassDemo.1 对应的就是当前第一个实现Runnable接口的匿名内部类

    // 静态代码块
    static {
        // 创建匿名内置类; 无入参无返回值
        new Runnable() {
            @Override
            public void run() {

            }
        };
    }

    // 实例代码块
    {
        // 无入参有返回值
        new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        };
    }

    public InnerClassDemo() {
        // 有入参有返回值
        new Comparable<String>() {
            @Override
            public int compareTo(String o) {
                return 0;
            }
        };
    }

    public static void main(String[] args) {
        // 有入参无返回值
        new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        };
    }
}
