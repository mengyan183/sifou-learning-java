/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * JarFileDemo
 * {@link java.util.jar.JarFile}
 *
 * @author guoxing
 * @date 2020/12/6 2:46 PM
 * @since
 */
public class JarFileDemo {

    public static void main(String[] args) throws IOException {
        /**
         * 由于 {@link java.util.jar.JarFile} 继承至 {@link java.util.zip.ZipFile}
         */
        /**
         * {@link org.apache.commons.io.IOUtils} 为例
         * 获取当前工程依赖的 commons-io jar
         */
        Class<IOUtils> ioUtilsClass = IOUtils.class;
        URL location = ioUtilsClass.getProtectionDomain()//获取当前类相关信息
                .getCodeSource()//获取当前类所属源码信息
                .getLocation();//获取源码所属路径
        try (JarFile jarFile = new JarFile(location.getFile())) {
            // 直接获取 commons-io 中的 META-INF/MANIFEST.MF 文件
            Manifest manifest = jarFile.getManifest();
            Attributes mainAttributes = manifest.getMainAttributes();
            for (Map.Entry<Object, Object> entry : mainAttributes.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }
}
