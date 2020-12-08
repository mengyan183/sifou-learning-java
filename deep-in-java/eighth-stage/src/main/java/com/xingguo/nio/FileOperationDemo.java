/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.nio;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FileOperationDemo
 * 文件相关操作: {@link java.nio.file.Files}
 *
 * @author guoxing
 * @date 2020/12/8 5:32 PM
 * @since
 */
public class FileOperationDemo {
    private static final String USER_DIR = System.getProperty("user.dir");

    public static void main(String[] args) throws IOException {
        /**
         * 判断文件是否存在
         * {@link java.nio.file.Files#exists(Path, LinkOption...)}  vs elder io {@link File#exists()}
         * 对于 {@link File#exists()} 实际是依赖于{@link java.io.FileSystem}调用native 方法来判断是否存在
         */
        System.out.printf("new io file exists:%s\n", Files.exists(Paths.get(USER_DIR)));
        System.out.printf("elder io file exists:%s\n", new File(USER_DIR).exists());
        /**
         * 文件访问性
         */
        System.out.printf("file \nisReadable:%s;\nisWritable:%s;\nisExecutable:%s\n", Files.isReadable(Paths.get(USER_DIR)), Files.isWritable(Paths.get(USER_DIR)), Files.isExecutable(Paths.get(USER_DIR)));
        /**
         * 文件 相等性
         * 对于{@link Path#isSameFile(Path, Path)}
         * 首先会调用 {@link Path#equals(Object)}方法判断是否相等,对于{@link Path}的concrete class {@link sun.nio.fs.UnixPath}(对于不同的系统存在不同的实现类)中的{@link sun.nio.fs.UnixPath#equals(Object)} 实际内部是调用的{@link sun.nio.fs.UnixPath#compareTo(Path)}方法,通过判断 路径字符串是否匹配; 如果是相同的路径则认为是相等的
         * 如果{@link Path#equals(Object)} {@code return false} ,其会获取{@link java.nio.file.attribute.BasicFileAttributes} 的 concrete class {@link UnixFileAttributes}(和操作系统有关) 通过对比具体相关文件属性进行一致性对比
         */
        System.out.printf("isSameFile:%s", Files.isSameFile(Paths.get(USER_DIR), Paths.get(USER_DIR)));
    }
}
