/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xing.level;

/**
 * JVMPrimaryDataDemo
 * jvm 中 原生类型数据存储
 *
 * @author guoxing
 * @date 2020/10/20 5:56 PM
 * @since
 */
public class JVMPrimaryDataDemo {
    // 对于当前常量数据,在类加载期间时将class文件转为Class对象后,初始化时会将当前数据存放到run-time constant pool 区域
    public static final int CONSTANT_COMPILER_DATA = 1;
    public int j = 4; // 对于该字段的数据是存放在构造方法中的;是存在于实例化对象中(堆)

    public static void main(String[] args) {
        // 而对于方法中的数据在运行时会被直接加载到栈中
        int i = 3; // 该数字在编译后会被翻译为 iconst_数值
    }
    /**
     * 对 编译后的class文件 使用javap -v 解析
     * // 常量池 == run-time constants pool
     * Constant pool:
     *    #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
     *    #2 = Class              #4             // java/lang/Object
     *    #3 = NameAndType        #5:#6          // "<init>":()V
     *    #4 = Utf8               java/lang/Object
     *    #5 = Utf8               <init>
     *    #6 = Utf8               ()V
     *    #7 = Class              #8             // com/xing/level/JVMPrimaryDataDemo
     *    #8 = Utf8               com/xing/level/JVMPrimaryDataDemo
     *    #9 = Utf8               CONSTANT_COMPILER_DATA // 字段名
     *   #10 = Utf8               I
     *   #11 = Utf8               ConstantValue
     *   #12 = Integer            1 // 声明的常量数据
     *   #13 = Utf8               Code
     *   #14 = Utf8               LineNumberTable
     *   #15 = Utf8               LocalVariableTable
     *   #16 = Utf8               this // 当前对象引用
     *   #17 = Utf8               Lcom/xing/level/JVMPrimaryDataDemo;
     *   #18 = Utf8               main // 方法名
     *   #19 = Utf8               ([Ljava/lang/String;)V
     *   #20 = Utf8               args // 参数名
     *   #21 = Utf8               [Ljava/lang/String;
     *   #22 = Utf8               i // 局部变量名
     *   #23 = Utf8               SourceFile
     *   #24 = Utf8               JVMPrimaryDataDemo.java
     */
}
