/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.nio;

import java.io.File;
import java.net.URI;
import java.nio.file.*;
import java.util.Objects;

/**
 * PathDemo
 * {@link java.nio.file.Path}
 *
 * @author guoxing
 * @date 2020/12/6 9:56 PM
 * @since
 */
public class PathDemo {
    // 对于 System工具类是阻塞线程操作,因此可以将其放在类信息中
    private final static String USER_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        pathInfo();
    }

    private static void pathInfo() {
        // 创建 path,利用 {@link Paths}工具类
        Path path = Paths.get(USER_DIR);
        String s = path.toString();
        System.out.println(s);
        System.out.println("====================");
        /**
         * 由于 {@link Path} 继承了 {@link Iterable},因此说明其存储了层次关系
         */
        int nameCount = path.getNameCount();
        // 遍历每一层path
        for (int i = 0; i < nameCount; i++) {
            Path name = path.getName(i);
            System.out.println(name);
        }
        System.out.println("====================");

        for (Path eachPath : path) {
            System.out.println(eachPath);
        }

        System.out.println("====================");

        System.out.printf("pathRoot:%s\n", path.getRoot());

        System.out.printf("pathParent:%s\n", path.getParent());

        /**
         * {@link Path#getFileName()} 实际是通过最后定位最后一个文件分隔符{@link File#separatorChar}进行往后截取
         */
        System.out.println(path.getFileName());
        String fileName = s.substring(s.lastIndexOf(File.separatorChar) + 1);
        System.out.println(fileName);
        System.out.println(Objects.equals(fileName, path.getFileName().toString()));
        //移除冗余路径
        // 对于 cd .. 操作实际就是返回文件上一级目录
        String parentS = s + "/..";
        // 对于 parentS 直接获取Path 实际得到的还是携带了 "/.." 操作符号的数据
        System.out.println(Paths.get(parentS));
        // 当调用 normalize后实际得到就是命令真正执行后的路径
        System.out.println(Paths.get(parentS).normalize());
        // 转换path
        File file = path.toFile();
        System.out.printf("pathToFile:%s\n", file);
        URI uri = path.toUri();
        System.out.printf("pathToUri:%s\n", uri);
        // 合并path
        // 获取到 path + "deep-in-java" 路径, 对于 resolve方法,如果传递的是一个拼接后不存在的路径,则会抛出异常
        Path resolve = path.resolve("deep-in-java");
        System.out.printf("resolvePath:%s\n", resolve);
        // 比较path
        System.out.printf("path:%s equals to fileToPath:%s :%s\n", path, file.toPath(), path.equals(file.toPath()));
        System.out.printf("path:%s equals to fileToPath:%s :%s\n", path, Paths.get(uri), path.equals(Paths.get(uri)));
    }
}
