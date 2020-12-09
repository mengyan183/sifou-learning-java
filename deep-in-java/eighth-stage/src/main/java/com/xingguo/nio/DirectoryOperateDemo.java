/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.Objects;

/**
 * DirectoryOperateDemo
 * {@link java.nio.file.Files#createDirectory(Path, FileAttribute[])} 当前操作只能在已存在的目录下创建文件
 * {@link java.nio.file.Files#createDirectories(Path, FileAttribute[])} 当前操作对于不存在的目录会按照父子关系依次创建
 *
 * @author guoxing
 * @date 2020/12/9 3:16 PM
 * @since
 */
public class DirectoryOperateDemo {
    private static final String USER_DIR = System.getProperty("user.dir");

    public static void main(String[] args) throws IOException {
//        directoryBasicOperate();
        // 遍历目录
        /**
         * {@link java.nio.file.FileVisitor}
         * {@link java.nio.file.FileSystem#getPathMatcher(String)} 支持正则匹配文件
         * 利用{@link Files#walkFileTree(Path, FileVisitor)} 进行文件查找
         */
        String javaProjectLocation = getCurrentJavaLocation();
        // 查找当前类所在目录下全部java文件
        findCommand(javaProjectLocation, "*.java");
//        findCommand(USER_DIR, "*.java");
    }

    /**
     * 获取maven工程下当前类的java文件全路径
     *
     * @return 当前java文件路径
     * @author guoxing
     * @date 2020-12-09 5:42 PM
     * @since 1.0.0
     */
    private static String getCurrentJavaLocation() {
        Class<DirectoryOperateDemo> directoryOperateDemoClass = DirectoryOperateDemo.class;
        String path = directoryOperateDemoClass.getResource("/").getPath();
        String projectPath = path.replace("/target/classes", "/src/main/java");
        Package aPackage = directoryOperateDemoClass.getPackage();
        String name = aPackage.getName();
        // 获取当前maven工程 java文件全路径
        return projectPath + name.replace(".", File.separator);
    }

    /**
     * 模拟 find 命令 查找匹配的文件
     *
     * @param pathName 查找根路径
     * @param pattern  正则表达式
     * @author guoxing
     * @date 2020-12-09 4:24 PM
     * @since 1.0.0
     */
    private static void findCommand(String pathName, String pattern) throws IOException {
        Path path = Paths.get(pathName);
        if (!Files.isDirectory(path)) {
            return;
        }
        CustomFileVisitor customFileVisitor = new CustomFileVisitor(pattern);
        try {
            Files.walkFileTree(path, customFileVisitor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("%s : 匹配到的文件数量为:%s\n", path, customFileVisitor.getCount());
    }

    /**
     * 自定义 文件查找类
     *
     * @author guoxing
     * @date 2020-12-09 4:30 PM
     * @since 1.0.0
     */
    static class CustomFileVisitor extends SimpleFileVisitor<Path> {
        // sun.nio.fs.UnixFileSystem.GLOB_SYNTAX
        private static final String syntax = "glob";
        // 采用文件正则匹配工具
        private final PathMatcher pathMatcher;
        // 返回匹配到的数据量
        private int count;

        public int getCount() {
            return count;
        }

        public CustomFileVisitor(String pattern) {
            this.pathMatcher = FileSystems.getDefault().getPathMatcher(syntax + ":" + pattern);
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            matchFile(dir);
            return FileVisitResult.CONTINUE;
        }

        private void matchFile(Path dir) {
            // dir.getFileName 获取的是当前文件名,而非全路径名
            if (Objects.nonNull(dir) && pathMatcher.matches(dir.getFileName())) {
                System.out.println(dir);
                count++;
            }
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            matchFile(file);
            return FileVisitResult.CONTINUE;
        }
    }


    private static void directoryBasicOperate() throws IOException {
        Path path = Files.createDirectory(Paths.get(USER_DIR, "temp"));
        Files.delete(path);
        // java.nio.file.NoSuchFileException
//        path = Files.createDirectory(Paths.get(USER_DIR, "temp", "secondtemp")); // 当前操作会抛出异常
        // 对于不存在的父级目录,会优先创建
        path = Files.createDirectories(Paths.get(USER_DIR, "temp", "secondtemp"));
        //遍历 目录 下的所有文件数据
        DirectoryStream<Path> paths = Files.newDirectoryStream(path, p -> p.toString().startsWith(USER_DIR));
        for (Path value : paths) {
            System.out.println(value);
        }

        // 由于创建了多级目录,因此需要循环删除
        // TODO:对于这种循环删除一定要确定终止条件
        Path basePath = Paths.get(USER_DIR);
        while (path.startsWith(USER_DIR) && !path.equals(basePath)) {
            Files.delete(path);
            path = path.getParent();
        }
    }
}
