/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.apt.compiler;

import lombok.extern.slf4j.Slf4j;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;

/**
 * CompilerDemo
 *
 * @author guoxing
 * @date 2020/11/25 8:58 PM
 * @since
 */
@Slf4j
public class CompilerDemo {
    public static void main(String[] args) throws IOException {
        /**
         * https://docs.oracle.com/en/java/javase/15/docs/api/java.compiler/javax/tools/JavaCompiler.html
         * 编译当前java文件
         * /Users/xingguo/sifou-learning-java/deep-in-java/seventh-stage/src/main/java/com/xingguo/apt/compiler/CompilerDemo.java
         */
        Class<CompilerDemo> compilerDemoClass = CompilerDemo.class;
        // 利用工具类 生成 javaCompiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 获得当前maven项目的 usr.dir
//        String projectPath = System.getProperty("user.dir");
        // 当前项目 target/classes 目录
        String path = compilerDemoClass.getResource("/").getPath();
        String projectPath = path.replace("/target/classes","/src/main/java");
        log.info(projectPath);
        // 获取CompilerDemo.java 的相对路径
        String name = compilerDemoClass.getName();
        log.info(name);
        // 拼接java文件相对路径
        String absolutePath = name.replace(".", File.separator).concat(".java");
        // 拼接当前文件在当前系统中的绝对路径
        String javaPath = projectPath.concat(absolutePath);
        // 加载当前文件
        File javaFile = new File(javaPath);
        // java文件管理工具
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        // 当前文件下的所有java文件对象
        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(javaFile);
        // 设置java文件的编译后的class输出目录,并非当前编译时依赖的classes路径,而是 javac -d 编译后的输出目录
        // 将当前编译后的class文件输出到当前 target/classes/custom-classes 目录下
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(getClassOutputDirectory(path)));
        JavaCompiler.CompilationTask task = compiler.getTask(new OutputStreamWriter(System.out), fileManager, null, null, null, javaFileObjects);
        task.call();

    }


    public static File getClassOutputDirectory(String path) {
        File currentClassPath = new File(path);
        File targetDirectory = currentClassPath.getParentFile();
        File classOutputDirectory = new File(targetDirectory, "custom-classes");
        // 生成类输出目录
        classOutputDirectory.mkdir();
        return classOutputDirectory;
    }
}
