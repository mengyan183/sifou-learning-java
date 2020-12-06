/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.legacy;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * ListFileCommandDemo
 * 模拟 macos "ls -lG" 命令输出
 *
 * @author guoxing
 * @date 2020/12/6 12:59 PM
 * @since
 */
public class ListFileCommandDemo {
    // 指定文件夹路径
    private final File rootDirectory;

    public ListFileCommandDemo(File rootDirectory) {
        if (Objects.isNull(rootDirectory) || rootDirectory.isFile()) {
            throw new RuntimeException();
        }
        this.rootDirectory = rootDirectory;
    }

    public void execute() {
        Stream.of(Objects.requireNonNull(rootDirectory.listFiles()))
                .forEach(file -> {
                    System.out.printf("size:%d,time:%s,name:%s\n",
                            file.length(),//文件大小,对于当前大小的统计受限于系统,对于当前得到的结果不一定是精确的
                            DateTimeFormatter.ISO_LOCAL_DATE_TIME
                                    .format(LocalDateTime.ofInstant(new Date(file.lastModified())
                                                    .toInstant(),
                                            ZoneId.systemDefault())), // 格式化时间
                            file.getName());// 获取文件名
                });

    }

    public static void main(String[] args) {
        // 解析当前项目第一层文件列表
        ListFileCommandDemo listFileCommandDemo = new ListFileCommandDemo(new File(System.getProperty("user.dir")));
        listFileCommandDemo.execute();
    }
}
