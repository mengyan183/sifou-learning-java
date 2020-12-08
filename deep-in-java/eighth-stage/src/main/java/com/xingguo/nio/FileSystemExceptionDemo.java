/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.nio;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * FileSystemExceptionDemo
 * 文件系统异常,{@link java.nio.file.FileSystemException} 作为更具体{@link java.io.IOException}
 * since 1.7 开始提供当前异常信息,为了配置File操作中的异常信息具体化,对于当前异常信息可以记录具体的文件操作异常
 *
 * @author guoxing
 * @date 2020/12/8 1:17 PM
 * @since
 */
public class FileSystemExceptionDemo {

    public static void main(String[] args) {
        /**
         * {@link java.nio.file.Files#notExists(Path, LinkOption...)}
         * 对于当前方法判断文件是否存在实际就是利用了{@link java.nio.file.NoSuchFileException} 来进行的判断操作
         */
        boolean b = Files.notExists(Paths.get(UUID.randomUUID().toString()));
        System.out.println(b);
    }
}
