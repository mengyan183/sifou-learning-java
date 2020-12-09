/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.nio;

import java.io.IOException;
import java.nio.file.*;

/**
 * CopyAndMoveDemo
 * {@link java.nio.file.Files#copy(Path, Path, CopyOption...)}
 * {@link java.nio.file.Files#move(Path, Path, CopyOption...)}
 *
 * @author guoxing
 * @date 2020/12/9 7:21 PM
 * @since
 */
public class CopyAndMoveDemo {
    private static final String USER_DIR = System.getProperty("user.dir");

    public static void main(String[] args) throws IOException {
        // 源文件
        Path sourcePath = Paths.get(USER_DIR, "README.md");
        // 创建目标文件夹
        Path copyDirectory = Paths.get(USER_DIR, "copy");
        Files.createDirectory(copyDirectory);

        Path moveDirectory = Paths.get(USER_DIR, "move");
//        Files.createDirectory(moveDirectory);

        // 测试复制文件夹
        /**
         * 对于复制文件操作
         * 1:当source为文件夹时,如果目标目录已存在,且选择{@link StandardCopyOption.REPLACE_EXISTING},则会调用{@link UnixNativeDispatcher#rmdir(sun.nio.fs.UnixPath)};如果目标目录不存在,则会创建新的文件夹{@link UnixCopyFile#copyDirectory(sun.nio.fs.UnixPath, sun.nio.fs.UnixFileAttributes, sun.nio.fs.UnixPath, sun.nio.fs.UnixCopyFile.Flags)}
         */
        Files.copy(copyDirectory, moveDirectory, StandardCopyOption.REPLACE_EXISTING);

        /**
         * 对于复制或移动文件操作,要求目标路径中的所有中间路径都存在,否则会抛出 {@link NoSuchFileException}
         */

        // 定义复制文件目标路径
        Path copyTargetPath = Paths.get(USER_DIR, "copy", "COPY-README.md");
        Files.copy(sourcePath, copyTargetPath, StandardCopyOption.REPLACE_EXISTING);
        // 定义移动文件目标路径
        Path moveTargetPath = Paths.get(USER_DIR, "move", "MOVE-README.md");
        Files.move(copyTargetPath, moveTargetPath, StandardCopyOption.REPLACE_EXISTING);
        // 删除移动后的文件
        Files.delete(moveTargetPath);
        // 删除文件夹
        Files.delete(moveDirectory);
        // 删除文件夹
        Files.delete(copyDirectory);

    }
}
