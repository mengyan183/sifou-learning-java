/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.apt.compiler;

import java.io.File;
import java.io.IOException;

/**
 * CustomCompilerDemo
 * 测试 {@link  com.xingguo.apt.compiler.Compiler}
 *
 * @author guoxing
 * @date 2020/11/25 8:58 PM
 * @since 1.0.0
 */
public class CustomCompilerDemo {

    public static void main(String[] args) throws IOException {
        // 获取当前maven 工程下的项目根路径
        String path = CustomCompilerDemo.class.getResource("/").getPath();
        String projectPath = path.replace("/target/classes", "/src/main/java");
        File sourcePath = new File(projectPath);
        // class 输出目录
        File targetClassPath = CompilerDemo.getClassOutputDirectory(path);
        Compiler compiler = new Compiler(sourcePath, targetClassPath);
        compiler.compiler(CustomCompilerDemo.class.getName());
    }
}
