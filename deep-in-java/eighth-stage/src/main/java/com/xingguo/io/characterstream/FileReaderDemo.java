/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.characterstream;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * FileReaderDemo
 *
 * @author guoxing
 * @date 2020/12/5 1:49 PM
 * @since
 */
//@Slf4j
public class FileReaderDemo {
    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        Class<FileReaderDemo> fileReaderDemoClass = FileReaderDemo.class;
        ClassLoader classLoader = fileReaderDemoClass.getClassLoader();
        String path = classLoader.getResource("").getPath();
//        log.info(path);
        System.out.println(path);
        // 读取当前java文件
        String javaPath = path.replace("/target/classes/", "/src/main/java/")
                .concat(fileReaderDemoClass.getName().replace(".", File.separator))
                .concat(".java");
        System.out.println(javaPath);
        FileReader fileReader = new FileReader(javaPath);
        String simpleName = fileReaderDemoClass.getSimpleName();

        FileWriter fileWriter = new FileWriter(javaPath.replace(simpleName, "Copy" + simpleName));
        char[] chars = new char[1024];
        while (fileReader.read(chars) != -1) {
            fileWriter.append(new String(chars));
            fileWriter.flush();
        }
        fileWriter.close();
        fileReader.close();
    }
}
