/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.reflection;

import java.lang.annotation.*;

/**
 * ClassObjects
 *
 * @author guoxing
 * @date 2020/11/10 2:15 PM
 * @since
 */
public class ClassObjects {
    /**
     * 通过 javap 可以看到
     *    #6 = Utf8               i
     *    #7 = Utf8               I
     *    #8 = Utf8               ConstantValue
     *    #9 = Integer            128
     *   #10 = Utf8               j
     *   #11 = Integer            1
     *
     *   对于当前基本类型常量实际对应的是 对应的包装类型
     */
    public static final int i = 128;
    public static final int j = 1;

    public static void main(String[] args) {
        primitives();
        // 对于基本类型的数组 是调用的 newarray 指令 并引用了基本类型
        // 5: newarray       int
        int[] intArray = new int[10];
        // 对于 对象类型的数组 是调用的 anewarray 指令,并引用指定的对象类型
        //10: anewarray     #3                  // class java/lang/Object
        Object[] objects = new Object[10];
        //16: anewarray     #4                  // class java/lang/Integer
        Integer[] integers = new Integer[10];
        /**
         * * <blockquote><pre>
         *  *     String str = "abc";
         *  * </pre></blockquote><p>
         *  * is equivalent to:
         *  * <blockquote><pre>
         *  *     char data[] = {'a', 'b', 'c'};
         *  *     String str = new String(data);
         *  * </pre></blockquote><p>
         */
        char[] c = new char[]{'a','b','c'};
        String str = new String(c);
        String abc = "abc"; // 对于当前对象实际是jvm将当前字符串转换为char数组并赋值给value字段
        char[] chars = abc.toCharArray();
    }

    /**
     * 对于原生类型的class实际就是原生类型的包装类
     *
     * 对于整型(int类型)数据的入栈操作 当int取值-1~5采用iconst指令，取值-128~127采用bipush指令，取值-32768~32767采用sipush指令，取值-2147483648~2147483647采用 ldc 指令。
     */
    public static void primitives() {
        /**
         *     0: getstatic     #3                  // Field java/lang/Integer.TYPE:Ljava/lang/Class;
         *          3: astore_0
         *          4: bipush        100
         *          6: istore_1
         *          7: return
         */
        Class<Integer> integerClass = int.class; // Integer.TYPE;
        int param = 100;
        // 对于当前数据会存在于constant pool 中
        long data = 1000L;
    }
}

/**
 * 对于当前枚举的class文件使用 javap -v 命令可以得到
 * final class com.xingguo.reflection.Color extends java.lang.Enum<com.xingguo.reflection.Color>
 * 可以看出枚举实际就是一个特殊的类 final class 枚举类名 extends @see java.lang.Enum<泛型为当前枚举的全路径>
 * 而且对于当前Color类而言还存在 构造方法 (Ljava/lang/String;I)V 内部是引用了 Enum#(Ljava/lang/String;I)V
 */
enum Color {
    /**
     * 对于这些常量实际就是
     * public static final com.xingguo.reflection.Color RED = new Color("RED",0);
     * public static final com.xingguo.reflection.Color YELLOW = new Color("YELLOW",1);
     * public static final com.xingguo.reflection.Color GREEN = new Color("GREEN",2);
     * <p>
     * 在对  static {} 代码块解析时可以看到这里使用 new 关键字调用了 Color#<init> 有参构造方法 Color(String name, int ordinal),对于第一个参数就是 定义的常量名称,而对于第二参数,则按照常量的顺序 从 0(iconst_0)开始到iconst_2,并赋值给对应的常量字段
     */
    RED,
    YELLOW,
    GREEN;
}

/**
 * 虽然通过对枚举的字节码解析可以看到 枚举 的真实的特性, 但如果直接像以下代码去编写一个类型的枚举,编译器会直接提示以下错误
 * <p>
 * Classes cannot directly extend 'java.lang.Enum'
 */
//final class ColorCopy extends Enum<ColorCopy>{
//    protected ColorCopy(String name, int ordinal) {
//        super(name, ordinal);
//    }
//}

/**
 * 通过 javap -v CustomOverride.class
 * 可以看到 interface com.xingguo.reflection.CustomOverride extends java.lang.annotation.Annotation
 * flags: (0x2600) ACC_INTERFACE, ACC_ABSTRACT, ACC_ANNOTATION
 *
 * 对于当前注解实际就是一个特殊的interface ,但是和普通的接口不同的点在于 对于当前类的标识中还存在一个ACC_ANNOTATION 标志,该标志的作用主要是在jvm解析时和普通的接口区分
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface CustomOverride {
}

/**
 * 在ElementType中实际并没有区分class和interface 统一都是 java.lang.annotation.ElementType#TYPE
 * 而对于注解类型 则 存在一个单独的定义 java.lang.annotation.ElementType#ANNOTATION_TYPE
 * 对当前自定义接口 通过javap 查看 class文件可以看到
 *  flags: (0x0600) ACC_INTERFACE, ACC_ABSTRACT
 *  在当前接口中并不存在ACC_ANNOTATION相关标识,因此当前在jvm解析阶段,他并不会存在注解相关的特性
 */
//@Target(ElementType.METHOD)
//@Retention(RetentionPolicy.RUNTIME)
interface CustomOverrideCopy extends Annotation {

}
