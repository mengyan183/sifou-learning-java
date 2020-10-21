/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xing.level;

import lombok.extern.slf4j.Slf4j;

/**
 * JVMStringDemo
 * 字符串字面数据处理过程
 *
 * @author guoxing
 * @date 2020/10/21 10:00 AM
 * @since
 */
@Slf4j
public class JVMStringDemo {
    public static final String CONSTANT_STRING = "a";
    public static final String CONSTANT_STRING_B = "b";
    public static final String CONSTANT_STRING_C = "c";
    // 对于 静态变量 在编译时 就直接被解析为最终结果
    public static final String CONSTANT_CONTACT = CONSTANT_STRING + CONSTANT_STRING_B;
    // 由于 对于常量 CONSTANT_STRING_B 和 CONSTANT_STRING_C 是固定的,因此对于其得到的结果也是固定的,所以在constant-pool中会存在其结果数据
    public String property = CONSTANT_STRING_B + CONSTANT_STRING_C;

    public static void main(String[] args) {
        /**
         * 对于 String#intern 的作用一般 对于 new String 对象才有用,
         * 对于 一个 new String 对象是存储在 heap中的;而对于字符串字面量是在编译阶段已经存储在 constant pool 区域中了,
         * 对于 String obj = new String("字面量"); 其结构实际为 在 heap 中 存在一个String 对象数据 内部又引用了 Constant pool 中字面量数据地址
         * 当对一个 new 得到的String 对象,当其引用调用了intern 方法时,实际当前引用会指向 constant pool 中 已存在常量数据
         *
         */
        String b = "b";
        log.info("string constant id:{}", System.identityHashCode(b));
        verify();

    }

    public static void verify() {
        String newString = new String("c");
        log.info("new string  id:{}", System.identityHashCode(newString));
        String intern = newString.intern();
        log.info("string intern id:{}", System.identityHashCode(intern));
        intern = "c";
        log.info("string intern id:{}", System.identityHashCode(intern));

        // 在constant pool 中 是不会存在 "ab"的;"ab" 是 运行时的结果;
        // 对于变量c以及其结果是直接存放在栈中的
        // 对于 c.intern 中,
        // 首先会执行lookup_shared 来查找堆中-constant pool中是否存在该字符串,如果存在,则会直接返回constant pool 中的内存地址;
        // 反之,则会do_lookup查找当前线程的栈中是否存在当前字符串数据,如果存在则会栈中的地址;
        // 反之,则会在当前线程栈中创建新的字符串数据,并返回新生成的地址
        String a = "a";
        String b = "b";
        String c = a + b;
        log.info("running new result id:{}", System.identityHashCode(c));
        log.info("running new result intern id:{};{}", System.identityHashCode(c.intern()), c == c.intern());

    }
    /**
     * 对于当前java中存在字符串字面量数据 "a"和"b"
     * 在 java.lang.String.java中 存在注释
     * The {@code String} class represents character strings.
     * All string literals in Java programs, such as {@code "abc"}, are implemented as instances of this class. // 对于"abc"等这种字符串字面量实际就是当前类型的实例
     * Strings are constant; // 字符串是一个常量
     *  对于 "are implemented as instances of this class" 这句话的实际是体现在前端编译阶段(编译为class文件)
     * 以下为当前 class文件的 编译结果
     * public class com.xing.level.JVMStringDemo
     *   minor version: 0
     *   major version: 59
     *   flags: (0x0021) ACC_PUBLIC, ACC_SUPER
     *   this_class: #9                          // com/xing/level/JVMStringDemo
     *   super_class: #2                         // java/lang/Object
     *   interfaces: 0, fields: 1, methods: 2, attributes: 1
     * Constant pool:
     *    #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
     *    #2 = Class              #4             // java/lang/Object
     *    #3 = NameAndType        #5:#6          // "<init>":()V
     *    #4 = Utf8               java/lang/Object
     *    #5 = Utf8               <init>
     *    #6 = Utf8               ()V
     *    #7 = String             #8             // b
     *    #8 = Utf8               b // 字符串字面量
     *    #9 = Class              #10            // com/xing/level/JVMStringDemo
     *   #10 = Utf8               com/xing/level/JVMStringDemo
     *   #11 = Utf8               CONSTANT_STRING
     *   #12 = Utf8               Ljava/lang/String;
     *   #13 = Utf8               ConstantValue
     *   #14 = String             #15            // a
     *   #15 = Utf8               a //字符串字面量
     *   #16 = Utf8               Code
     *   #17 = Utf8               LineNumberTable
     *   #18 = Utf8               LocalVariableTable
     *   #19 = Utf8               this
     *   #20 = Utf8               Lcom/xing/level/JVMStringDemo;
     *   #21 = Utf8               main
     *   #22 = Utf8               ([Ljava/lang/String;)V
     *   #23 = Utf8               args
     *   #24 = Utf8               [Ljava/lang/String;
     *   #25 = Utf8               SourceFile
     *   #26 = Utf8               JVMStringDemo.java
     * {
     *   public static final java.lang.String CONSTANT_STRING;
     *     descriptor: Ljava/lang/String;
     *     flags: (0x0019) ACC_PUBLIC, ACC_STATIC, ACC_FINAL
     *     ConstantValue: String a
     *
     *   public com.xing.level.JVMStringDemo();
     *     descriptor: ()V
     *     flags: (0x0001) ACC_PUBLIC
     *     Code:
     *       stack=1, locals=1, args_size=1
     *          0: aload_0
     *          1: invokespecial #1                  // Method java/lang/Object."<init>":()V
     *          4: return
     *       LineNumberTable:
     *         line 14: 0
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             0       5     0  this   Lcom/xing/level/JVMStringDemo;
     *
     *   public static void main(java.lang.String[]);
     *     descriptor: ([Ljava/lang/String;)V
     *     flags: (0x0009) ACC_PUBLIC, ACC_STATIC
     *     Code:
     *       stack=1, locals=2, args_size=1
     *          0: ldc           #7                  // String b
     *          2: astore_1
     *          3: return
     *       LineNumberTable:
     *         line 18: 0
     *         line 19: 3
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             0       4     0  args   [Ljava/lang/String;
     *             3       1     1     b   Ljava/lang/String;
     * }
     * 在 classLoad加载当前class文件转换为 class对象时,
     * 在 "src/hotspot/share/classfile/classFileParser.cpp"存在
     * ClassFileParser对象实例化时会调用
     * -> parse_stream 解析class二进制文件流数据
     * -> parse_constant_pool 解析 constant_pool相关数据
     * -> parse_constant_pool_entries 在该方法中会解析 class文件中的 Constant pool 中的数据,在这里会根据索引(#数值)来解析数据, 并且会使用数组存储当前数据
     * CONSTANT_String_info {
     *      u1 tag; // src/hotspot/share/utilities/constantTag.hpp 中定义的JVM_CONSTANT_ 相关数据
     *      u2 string_index; // 当前数据在 constant pool 的索引值
     * }
     * -> 在后续的for 循环迭代时 ,当匹配到 JVM_CONSTANT_StringIndex 时,这里实际是存储字符串数据的引用数据
     *
     */
}
