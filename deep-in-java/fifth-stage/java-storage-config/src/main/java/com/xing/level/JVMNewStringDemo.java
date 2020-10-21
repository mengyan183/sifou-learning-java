/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xing.level;

/**
 * JVMNewStringDemo
 * 关键字 new 的内存分配逻辑
 * 以及 特殊的 对象 String 内存分配逻辑
 *
 * @author guoxing
 * @date 2020/10/20 8:20 PM
 * @since
 */
public class JVMNewStringDemo {
    public final static String STRING = "FINAL_STRING";


    public static void main(String[] args) {


    }

    public static void newObject() {
        Object o = new Object();
    }

    public static void stringObject() {
        String a = "a";
        String aObject = new String("b");
    }

    /**
     * 首先来分析一下当前类的class 文件
     * public class com.xing.level.JVMNewStringDemo
     *   minor version: 0
     *   major version: 59
     *   flags: (0x0021) ACC_PUBLIC, ACC_SUPER
     *   this_class: #16                         // com/xing/level/JVMNewStringDemo
     *   super_class: #2                         // java/lang/Object
     *   interfaces: 0, fields: 1, methods: 4, attributes: 1
     * Constant pool: // 常量池
     *    #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
     *    #2 = Class              #4             // java/lang/Object
     *    #3 = NameAndType        #5:#6          // "<init>":()V
     *    #4 = Utf8               java/lang/Object
     *    #5 = Utf8               <init>
     *    #6 = Utf8               ()V
     *    #7 = String             #8             // a
     *    #8 = Utf8               a // 字符串数据 a
     *    #9 = Class              #10            // java/lang/String
     *   #10 = Utf8               java/lang/String
     *   #11 = String             #12            // b
     *   #12 = Utf8               b // 字符串数据 b
     *   #13 = Methodref          #9.#14         // java/lang/String."<init>":(Ljava/lang/String;)V
     *   #14 = NameAndType        #5:#15         // "<init>":(Ljava/lang/String;)V
     *   #15 = Utf8               (Ljava/lang/String;)V
     *   #16 = Class              #17            // com/xing/level/JVMNewStringDemo
     *   #17 = Utf8               com/xing/level/JVMNewStringDemo
     *   #18 = Utf8               STRING // 常量名称
     *   #19 = Utf8               Ljava/lang/String;
     *   #20 = Utf8               ConstantValue
     *   #21 = String             #22            // FINAL_STRING
     *   #22 = Utf8               FINAL_STRING //字符串数据 FINAL_STRING
     *   #23 = Utf8               Code
     *   #24 = Utf8               LineNumberTable
     *   #25 = Utf8               LocalVariableTable
     *   #26 = Utf8               this
     *   #27 = Utf8               Lcom/xing/level/JVMNewStringDemo;
     *   #28 = Utf8               main
     *   #29 = Utf8               ([Ljava/lang/String;)V
     *   #30 = Utf8               args
     *   #31 = Utf8               [Ljava/lang/String;
     *   #32 = Utf8               newObject
     *   #33 = Utf8               o
     *   #34 = Utf8               Ljava/lang/Object;
     *   #35 = Utf8               stringObject
     *   #36 = Utf8               aObject
     *   #37 = Utf8               SourceFile
     *   #38 = Utf8               JVMNewStringDemo.java
     * {
     *   public static final java.lang.String STRING;
     *     descriptor: Ljava/lang/String;
     *     flags: (0x0019) ACC_PUBLIC, ACC_STATIC, ACC_FINAL
     *     ConstantValue: String FINAL_STRING
     *
     *   public com.xing.level.JVMNewStringDemo();
     *     descriptor: ()V
     *     flags: (0x0001) ACC_PUBLIC
     *     Code:
     *       stack=1, locals=1, args_size=1
     *          0: aload_0
     *          1: invokespecial #1                  // Method java/lang/Object."<init>":()V
     *          4: return
     *       LineNumberTable:
     *         line 15: 0
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             0       5     0  this   Lcom/xing/level/JVMNewStringDemo;
     *
     *   public static void main(java.lang.String[]);
     *     descriptor: ([Ljava/lang/String;)V
     *     flags: (0x0009) ACC_PUBLIC, ACC_STATIC
     *     Code:
     *       stack=0, locals=1, args_size=1
     *          0: return
     *       LineNumberTable:
     *         line 22: 0
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             0       1     0  args   [Ljava/lang/String;
     *
     *   public static void newObject();
     *     descriptor: ()V
     *     flags: (0x0009) ACC_PUBLIC, ACC_STATIC
     *     Code:
     *       stack=2, locals=1, args_size=0
     *          0: new           #2                  // class java/lang/Object
     *          3: dup
     *          4: invokespecial #1                  // Method java/lang/Object."<init>":()V
     *          7: astore_0
     *          8: return
     *       LineNumberTable:
     *         line 25: 0
     *         line 26: 8
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             8       1     0     o   Ljava/lang/Object;
     *
     *   public static void stringObject();
     *     descriptor: ()V
     *     flags: (0x0009) ACC_PUBLIC, ACC_STATIC
     *     Code:
     *       stack=3, locals=2, args_size=0
     *       //对于 ldc 实际是调用的 src/hotspot/share/classfile/bytecodeAssembler.cpp 中的 ldc方法,其操作实际是将字节码中的代码进行关联;对于该cpp文件中的实现实际就是部分汇编的实现
     *       // 例如此处就是 关联 constant pool 中的 #7 位置数据;
     *          0: ldc           #7                  // String a
     *          2: astore_0 // 该处是入栈操作
     *          // 该操作是调用src/hotspot/share/memory/allocation.hpp 中的 CHeapObj#new 方法,并引用 class java/lang/String 类型
     *          3: new           #9                  // class java/lang/String
     *          6: dup
     *          7: ldc           #11                 // String b
     *          //invokespecial https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.invokespecial
     *          // 表示的是调用当前实例的方法;一般指代当前类的父类/私有/实例初始化方法
     *          // 在此处调用的是 java.lang.String#构造方法(实例初始化方法)
     *          9: invokespecial #13                 // Method java/lang/String."<init>":(Ljava/lang/String;)V
     *         12: astore_1
     *         13: return
     *       LineNumberTable:
     *         line 29: 0
     *         line 30: 3
     *         line 31: 13
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             3      11     0     a   Ljava/lang/String;
     *            13       1     1 aObject   Ljava/lang/String;
     * }
     */

    /**
     * 对于 new 关键字的分析
     * 在new 操作执行时 会在 heap 区域开辟空间存放实例数据;
     * 在 src/hotspot/share/memory/allocation.hpp 的 CHeapObj 存在对应的new 方法 以及 new[] 方法;第一个表示为对象 ,第二个表示为数组
     * 在new 中实际是调用 当前文件中的 AllocateHeap 方法,在 其方法中 实际 就是调用的 os::malloc 内存分配方法;当内存不足时会分配失败
     */
}
