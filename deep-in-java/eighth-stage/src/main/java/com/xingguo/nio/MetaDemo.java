/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.nio;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserPrincipal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import static com.xingguo.nio.DirectoryOperateDemo.getCurrentJavaLocation;

/**
 * MetaDemo
 *
 * @author guoxing
 * @date 2020/12/9 7:23 PM
 * @since
 */
public class MetaDemo {
    private static final String USER_DIR = System.getProperty("user.dir");

    public static void main(String[] args) throws IOException, URISyntaxException {
        String currentJavaLocation = getCurrentJavaLocation();
        Path path = Paths.get(currentJavaLocation).resolve(MetaDemo.class.getSimpleName() + ".java");
        // 文件大小
        // 针对于文件夹类型的 file 当获取文件大小时,实际并不会获取文件夹下所有文件以及文件夹的大小
        /**
         * 和 {@link File#length()} 效果实际是一致的
         */
        long size = Files.size(path);
        System.out.printf("%s's size: %s bytes\n", path, size);
        // 获取指定文件夹的全部数据
        System.out.printf("PATH : %s's size is %s bytes\n", USER_DIR, getSize(Paths.get(USER_DIR)));

        /**
         * 最后修改时间
         * {@link Files#getLastModifiedTime(Path, LinkOption...)}
         * 获取target目录的最新编辑时间
         */
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        FileTime lastModifiedTime = Files.getLastModifiedTime(Paths.get(Objects.requireNonNull(contextClassLoader.getResource("")).toURI()));
        System.out.println(lastModifiedTime);

        /**
         * 获取文件权限
         * {@link Files#getOwner(Path, LinkOption...)}
         */
        UserPrincipal owner = Files.getOwner(Paths.get(USER_DIR));
        System.out.printf("%s's owner is %s\n", USER_DIR, owner);

        /**
         * 存储文件属性
         * {@link Files#getAttribute(Path, String, LinkOption...)}
         */
        BasicFileAttributes attrs = Files.readAttributes(Paths.get(USER_DIR), BasicFileAttributes.class);
        System.out.printf("%s is directory: %s\n", Paths.get(USER_DIR), attrs.isDirectory());
        attrs = Files.readAttributes(path, BasicFileAttributes.class);
        System.out.printf("%s is file: %s\n", path, attrs.isRegularFile());

    }

    /**
     * 由于{@link Files#size(Path)}对于文件夹的容量并不会直接获取所有子级的文件数据,因此可以采用分治或递归来对文件夹进行特殊处理
     *
     * @param path 路径
     * @return 文件总大小
     * @author guoxing
     * @date 2020-12-09 8:56 PM
     * @since 1.0.0
     */
    private static long getSize(Path path) throws IOException {
        AtomicLong size = new AtomicLong();
        if (Files.isDirectory(path)) {
            Files.newDirectoryStream(path)
                    .forEach(subPath -> {
                        try {
                            size.addAndGet(getSize(subPath));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } else if (Files.isRegularFile(path)) {
            size.addAndGet(Files.size(path));
        }
        return size.get();
    }
}
