/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.legacy;

import java.io.File;
import java.io.FileFilter;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * FileSizeDemo
 *
 * @author guoxing
 * @date 2020/12/6 1:24 PM
 * @since
 */
public class FileSizeDemo {
    private final File root;
    //支持对具体的文件进行条件过滤
    private Predicate<File> filePredicate;

    public FileSizeDemo(File root, FileFilter... fileFilters) {
        if (Objects.isNull(root)) {
            throw new RuntimeException();
        }
        this.root = root;
        this.filePredicate = new FilePredicate(fileFilters);
    }

    public FileSizeDemo(File root, Predicate<File> filePredicate) {
        this.root = root;
        this.filePredicate = filePredicate;
    }

    /**
     * 自定义 filePredicate类
     */
    private class FilePredicate implements Predicate<File> {
        private final FileFilter[] fileFilters;

        public FilePredicate(FileFilter[] fileFilters) {
            this.fileFilters = fileFilters;
        }

        @Override
        public boolean test(File file) {
            if (Objects.isNull(file)) {
                throw new RuntimeException();
            }
            if (Objects.nonNull(fileFilters)) {
                for (FileFilter fileFilter : fileFilters) {
                    if (Objects.nonNull(fileFilter)) {
                        if (!fileFilter.accept(file)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    // 默认单位为 bytes
    public long getSize() {
        // 采用分治思想
        if (root.isFile() && filePredicate.test(root)) {
            return root.length();
        } else if (root.isDirectory()) {
            long size = 0L;
            File[] files = root.listFiles();
            assert files != null;
            size += Stream.of(files)
                    .filter(File::isFile)
                    .filter(filePredicate)
                    .mapToLong(File::length)
                    .sum();
            size += Stream.of(files)
                    .filter(File::isDirectory)
                    .mapToLong(file -> new FileSizeDemo(file, filePredicate).getSize())
                    .sum();
            return size;
        }
        return 0L;
    }

    public static void main(String[] args) {
        // 获取当前工程的文件中全部java文件的总大小
        FileSizeDemo fileSizeDemo = new FileSizeDemo(new File(System.getProperty("user.dir")), file -> (file.isFile() && file.getName().endsWith(".java")));

        System.out.println(fileSizeDemo.getSize() / 1024 + " KB");
    }
}
