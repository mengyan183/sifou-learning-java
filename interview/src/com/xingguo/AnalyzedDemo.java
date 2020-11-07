/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo;

/**
 * AnalyzedDemo
 * 分析一个java进程启动时发生的事情
 *
 * @author guoxing
 * @date 2020/11/6 4:51 PM
 * @since
 */
public class AnalyzedDemo {
    /**
     * 启动当前java进程main方法执行到进程消亡 过程分析
     * 1:前端编译,首先需要将当前java文件编译为class文件,一般说java是静态语言,主要体现在需要通过class二进制文件进行操作
     * 2:对于前端编译后的class文件,通过javap命令可以看到其包含当前类的相关信息,以及类中相关方法等静态数据信息
     * 3:创建java进程,对于进程代表的是一个操作系统中运行程序的最小单元,对于java进程而言实际就是线程的集合; 在这一步还需要后端编译,就是将class文件编译为jvm可操作的数据;在openjdk的源码中存在一个byteArrayDecode的cpp文件,通过解析class文件是实现JMM的搭建
     * 4:在jvm的区域中主要包含 : fullGC 是针对整个区域
     *      method-area(包含了 run-time constant pool):在当前区域主要包含类加载相关,主要是存储解析后的class数据;属于全部jvm线程共享的
     *      heap  :堆区主要存储java对象和数组;对于当前区域主要是 gc 相关的分代操作; 一般当前区域经常发生的 minor gc 和 major gc;
     *      pc-registers :这些都是属于线程私有的,每个线程都会拥有一个单独的pc-register,主要是记录线程在运行期间的相关方法以及运行时产生的中间数据
     *      jvm-threadStacks : 当前区域也是jvm线程私有的,每当一个jvm线程执行方法时,都会创建一个私有的栈,对于方法的执行实际就是一个压栈和出栈的过程
     *      NativeMethodStacks: 对于java而言,在运行时不一定只会执行java方法,还有可能执行native方法,对于native方法一般指代的就是c或cpp代码,对于当前语言代码的运行,在jvm中存在单独的C-stack空间
     * 5:随着主线程执行的结束以及消亡,当前java进程也紧接着消亡,对于java程序而言,只要主线程不结束,当前java线程就不会主动结束
     *
     * @param args
     */
    public static void main(String[] args) {
        print();
    }

    public static void print() {
        System.out.println("1");
    }
}
