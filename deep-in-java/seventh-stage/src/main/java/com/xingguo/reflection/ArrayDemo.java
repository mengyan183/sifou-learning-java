/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.reflection;

/**
 * ArrayDemo
 *
 * @author guoxing
 * @date 2020/11/11 4:49 PM
 * @since
 */
public class ArrayDemo {

    /**
     *  public static void main(java.lang.String[]);
     *     descriptor: ([Ljava/lang/String;)V
     *     flags: (0x0009) ACC_PUBLIC, ACC_STATIC
     *     Code:
     *       stack=1, locals=3, args_size=1
     *          0: iconst_3
     *          1: newarray       int
     *          3: astore_1
     *          4: aload_1
     *          5: arraylength
     *          6: istore_2
     *          7: return
     *       LineNumberTable:
     *         line 16: 0
     *         line 17: 4
     *         line 18: 7
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             0       8     0  args   [Ljava/lang/String;
     *             4       4     1  ints   [I
     *             7       1     2 length   I
     * }
     * @param args
     */
    public static void main(String[] args) {
        int[] ints = new int[3];
        int length = ints.length; //arraylength jvm 指令
    }
}
