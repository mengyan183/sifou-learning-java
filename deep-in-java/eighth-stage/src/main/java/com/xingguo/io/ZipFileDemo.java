/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ZipFileDemo
 * {@link java.util.zip.ZipFile}
 *
 * @author guoxing
 * @date 2020/12/6 2:46 PM
 * @since
 */
public class ZipFileDemo {
    public static void main(String[] args) throws IOException {
        /**
         * {@link org.apache.commons.io.IOUtils} 为例
         * 获取当前工程依赖的 commons-io jar
         */
        Class<IOUtils> ioUtilsClass = IOUtils.class;
        URL location = ioUtilsClass.getProtectionDomain()//获取当前类相关信息
                .getCodeSource()//获取当前类所属源码信息
                .getLocation();//获取源码所属路径
        System.out.println(location);
        // 创建一个zip文件
        try (ZipFile zipFile = new ZipFile(location.getFile(), StandardCharsets.UTF_8)) {
            // 打印zip中的所有文件(文件夹和文件)
            // 对于 zip文件在java中的数据存储格式实际是按照每一层文件进行平铺开的, 每一层都是一个 {@link java.util.zip.ZipEntry}
            zipFile.stream()
                    .forEach(System.out::println);
            // 获取 commons-io 中的 META-INF/MANIFEST.MF 文件
            ZipEntry entry = zipFile.getEntry("META-INF/MANIFEST.MF");
            if (!entry.isDirectory()) {
                // 将当前entry转换为 输入流
                try (InputStream inputStream = zipFile.getInputStream(entry)) {
                    // 将输入流转换为reader 并输出数据
                    String s = IOUtils.toString(new InputStreamReader(inputStream));
                    System.out.println(s);
                }
            }
        }
    }
}
