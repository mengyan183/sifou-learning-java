/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.apt.annotation.process;

import com.xingguo.apt.compiler.Compiler;
import com.xingguo.apt.compiler.CompilerDemo;
import com.xingguo.apt.compiler.CustomCompilerDemo;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * RepositoryAnnotationProcessorDemo
 *
 * @author guoxing
 * @date 2020/11/26 8:16 PM
 * @since
 */
@Component
public class RepositoryAnnotationProcessorDemo {

    public static void main(String[] args) throws IOException {
        // 获取当前maven 工程下的项目根路径
        String path = CustomCompilerDemo.class.getResource("/").getPath();
        String projectPath = path.replace("/target/classes", "/src/main/java");
        File sourcePath = new File(projectPath);
        // class 输出目录
        File targetClassPath = CompilerDemo.getClassOutputDirectory(path);
        Compiler compiler = new Compiler(sourcePath, targetClassPath);
        compiler.compiler("com.xingguo.reflection.application.UserRepository",
                "com.xingguo.reflection.application.User",
                "com.xingguo.reflection.application.Repository",
                "com.xingguo.reflection.application.UserRepositoryImpl",
                "com.xingguo.apt.annotation.process.RepositoryAnnotationProcessorDemo",
                "com.xingguo.apt.annotation.process.RepositoryAnnotationProcessor"
        );
    }
}
