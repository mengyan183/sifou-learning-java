/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.apt.compiler;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Compiler
 * 基于{@see javax.tools.JavaCompiler} 封装
 *
 * @author guoxing
 * @date 2020/11/25 8:58 PM
 * @since 1.0.0
 */
public class Compiler {

    // 目标源文件根目录; javac -sourcepath
    private final File sourcePth;

    // 编译后的class文件输出目录 javac -d <directory>               Specify where to place generated class files
    private final File targetClassPath;

    private final StandardJavaFileManager standardJavaFileManager;

    private final JavaCompiler javaCompiler;

    public Compiler(File sourcePth, File targetClassPath) {
        this.sourcePth = sourcePth;
        this.targetClassPath = targetClassPath;
        // 获取编译器
        this.javaCompiler = ToolProvider.getSystemJavaCompiler();
        // 获取标准文件管理工具
        this.standardJavaFileManager = javaCompiler.getStandardFileManager(null, null, null);
    }

    /**
     * 根据 sourcePath 和 指定的className 来查找当前指定的java文件,
     * 并将其编译输出到指定的目录下
     *
     * @param classNames 多个全路径类名
     * @throws IOException 异常信息
     */
    public void compiler(String... classNames) throws IOException {
        if (CollectionUtils.isEmpty(Arrays.asList(classNames))) {
            return;
        }

        Iterable<? extends JavaFileObject> javaFileObjects = standardJavaFileManager.getJavaFileObjects(Stream.of(classNames)
                .filter(className -> !StringUtils.isEmpty(className))
                .map(className -> {
                    String javaFileName = className.replace('.', File.separatorChar).concat(".java");
                    // 查找当前java文件
                    return new File(sourcePth, javaFileName);
                }).toArray(File[]::new));
        // 设置编译后的class输出目录
        standardJavaFileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(targetClassPath));
        // 创建编译任务
        JavaCompiler.CompilationTask task = javaCompiler.getTask(new OutputStreamWriter(System.out), standardJavaFileManager, null, null, null, javaFileObjects);
        // 手动指定 processor
//        task.setProcessors();
        // 执行任务
        task.call();
    }

}
