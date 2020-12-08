/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * TryWithResourceDemo
 * {@link AutoCloseable} with try-with-resources
 *
 * @author guoxing
 * @date 2020/12/8 12:41 PM
 * @since 1.0.0
 */
public class TryWithResourceDemo {
    // 获取 当前项目运行根目录
    private static final String USER_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        //  获取当前项目下的readMe文件路径
        Path readMePath = Paths.get(USER_DIR, "README.md");
        tryWithResources(readMePath);
        tryWithFinally(readMePath);
    }

    /**
     * 对于 当前方法 和 {@link #tryWithResources}实际是相同的效果,但 try-with-resources 代码明显在编写时精简了许多
     *
     * @param readMePath
     */
    private static void tryWithFinally(Path readMePath) {
        BufferedReader bufferedReader = null;
        try {
            try {
                bufferedReader = Files.newBufferedReader(readMePath);
                for (String line = bufferedReader.readLine(); Objects.nonNull(line); line = bufferedReader.readLine()) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (Objects.nonNull(bufferedReader)) {
                    bufferedReader.close();
                }
            }
        } catch (IOException e) {
            // 捕获资源释放的异常
            throw new RuntimeException(e);
        }

    }

    private static void tryWithResources(Path readMePath) {
        // 使用buffered reader 读取文件,借助Files工具类生成BufferedReader
        /**
         * 通过idea class 反编译工具以及 javap 等命令可以看到 当前端编译后jdk字节码提升的代码
         * 根据 try -with - resources 语法,在class文件中实际会自动生成 {@link AutoCloseable#close()}相关代码来释放资源
         * 通过 javap class文件 中可以看到 invokevirtual(其代表的是属于字节码提升)
         * 根据多态的特性,其会调用具体实现类中的close方法
         */
        try (BufferedReader bufferedReader = Files.newBufferedReader(readMePath)) {
            for (String line = bufferedReader.readLine(); Objects.nonNull(line); line = bufferedReader.readLine()) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
