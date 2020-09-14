/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional;

/**
 * ActionDemo
 * 隐藏类型;单纯的执行
 *
 * @author guoxing
 * @date 9/14/20 11:49 AM
 * @since
 */
public class ActionDemo {

    public static void main(String[] args) {
        // 查看当前类的编译后的文件, 实际会生成两个class 文件
        // ActionDemo.class  ActionDemo$1.class
        // 在通过 javap -v ActionDemo.class 中 查看对 main方法的分析
        /**
         * public static void main(java.lang.String[]);
         *     descriptor: ([Ljava/lang/String;)V
         *     flags: (0x0009) ACC_PUBLIC, ACC_STATIC
         *     Code:
         *       stack=2, locals=3, args_size=1
         *          0: invokedynamic #7,  0              // InvokeDynamic #0:run:()Ljava/lang/Runnable;
         *          5: astore_1
         *          6: new           #11                 // class com/xingguo/first/functional/ActionDemo$1
         *          9: dup
         *         10: invokespecial #13                 // Method com/xingguo/first/functional/ActionDemo$1."<init>":()V
         *         13: astore_2
         *         14: return
         *       LineNumberTable:
         *         line 17: 0
         *         line 19: 6
         *         line 25: 14
         *       LocalVariableTable:
         *         Start  Length  Slot  Name   Signature
         *             0      15     0  args   [Ljava/lang/String;
         *             6       9     1 runnable   Ljava/lang/Runnable;
         *            14       1     2 runnable1   Ljava/lang/Runnable;
         * }
         */
        // 该行 函数式编程 实际是  0: invokedynamic #7,  0              // InvokeDynamic #0:run:()Ljava/lang/Runnable;
        // invokedynamic指令实际是since JDK1.7 ; 和当前指令相关联的以下两个类
        // java.lang.invoke.InvokeDynamic
        // java.lang.invoke.MethodHandle
        Runnable runnable = () -> System.out.println("1");

        // 而该行代码实际是编译后先生成了匿名内置类
        /**
         * class ActionDemo$1 implements Runnable {
         *     ActionDemo$1() {
         *     }
         *
         *     public void run() {
         *         System.out.println("1");
         *     }
         * }
         */
        // 再通过创建匿名内置类对象; 通过invokespecial来关联匿名内置类的实现方法
        /**
         * *          6: new           #11                 // class com/xingguo/first/functional/ActionDemo$1
         *          *          9: dup
         *          *         10: invokespecial #13                 // Method com/xingguo/first/functional/ActionDemo$1."<init>":()V
         */
        // 可以转换为
//        Runnable runnable1 = new ActionDemo$1();
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("1");
            }
        };
    }
}
