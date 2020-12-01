/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.instrument;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * ManifestDemo
 *
 * @author guoxing
 * @date 2020/11/29 8:22 PM
 * @since
 */
@Slf4j
public class ManifestDemo {
    public static void main(String[] args) throws IOException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        // 获取当前加载路径
        String path = contextClassLoader.getResource("").getPath();
        log.info("{}", path);
        //TODO:也有可能从 jar 中进行读取

        // 从当前文件路径进行加载
        File file = new File(path.concat("/META-INF/MANIFEST.MF"));
        if (file.exists() && file.canRead() && file.isFile()) {
            Manifest manifest = new Manifest(new FileInputStream(file));
            Attributes mainAttributes = manifest.getMainAttributes();
            mainAttributes.forEach((key, value) -> log.info("{}:{}", key, value));
        }
    }
}
