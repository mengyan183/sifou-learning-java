/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.operate;

import java.io.IOException;

/**
 * CommandLineDemo
 *
 * @author guoxing
 * @date 2020/12/5 4:57 PM
 * @since
 */
public class CommandLineDemo {
    public static void main(String[] args) throws IOException {
        System.out.println("please any key to exit .....");
        // 对于当前阻塞操作,实际是利用了{@link java.io.FileInputStream#readBytes}
        // 通过调用jni 利用 {@link FileInputStream#Java_java_io_FileInputStream_readBytes}来实现bio数据读取
        System.in.read();
        // 当前代码只能在命令行中执行
        /**
         * If this virtual machine has a console then it is represented by a unique instance of this class which can be obtained by invoking the System.console() method. If no console device is available then an invocation of that method will return null.
         */
//        Console console = System.console();
//        console.printf("Hello%s", "World");
    }
}
