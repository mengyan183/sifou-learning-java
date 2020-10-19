/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xing.level;

import lombok.extern.slf4j.Slf4j;

import java.security.CodeSource;

/**
 * ClassPathAndClassLoaderDemo
 *
 * @author guoxing
 * @date 2020/10/19 11:13 AM
 * @since
 */
@Slf4j
public class ClassPathAndClassLoaderDemo {
    public static void main(String[] args) throws ClassNotFoundException {
        // 在启动命令中增加 -verbose:class 打印当前类启动记载的所有class所在的文件夹
        /**
         * [0.137s][info][class,load] com.xing.level.ClassPathAndClassLoaderDemo source: file:/Users/xingguo/sifou-learning-java/deep-in-java/fifth-stage/java-storage-config/target/classes/
         * 对于当前类可以看到其class所处的文件夹
         */
        /**
         * file:/Users/xingguo/sifou-learning-java/deep-in-java/fifth-stage/java-storage-config/target/classes/
         * 可以看到这里也会打印出当前class文件路径
         * 其打印结果还代表 file 协议,表示其为一个file协议的URL
         * 在 sun.net.www.protocol 包下存在不同的内置协议的Handler
         * 对于所有的不同的协议的实际都属于URL因此在java.net.URL#openConnection(java.net.Proxy)根据不同的handler实现来实现
         */
        printClassPath(ClassPathAndClassLoaderDemo.class);
        /**
         * 对于 使用BootStrap classLoader 加载的class 是不能找到其codeSource 路径的
         */
        printClassPath(Object.class);

        /**
         * 对于  jdk.internal.loader.ClassLoaders$AppClassLoader#loadClass的过程实际为
         *  AppClassLoader#loadClass(java.lang.String, boolean) -> BuiltinClassLoader#loadClass(java.lang.String, boolean)
         *  -> BuiltinClassLoader#loadClassOrNull(java.lang.String, boolean) (如果当前classLoader为BootClassLoader则调用BootClassLoader#loadClassOrNull(java.lang.String, boolean))
         *  -> ClassLoader#findLoadedClass(java.lang.String) 校验当前ClassLoader是否已加载过当前class -> 如果已加载则直接返回
         *  ->反之,则会执行BuiltinClassLoader#findLoadedModule(java.lang.String) 模块化加载查找 或 递归查找parentClassLoader 直到找到已加载的class或parent为空 -> 如果 找到已加载的class则会直接返回
         *  -> 反之,则 BuiltinClassLoader#findClassOnClassPathOrNull(java.lang.String) 根据classPath进行URL查找,如果能查找到指定的class文件,则会解析文件并生成Class对象,反之则返回空
         */
        /**
         * 由于当前工程为module化工程, 以下代码的执行顺序为 首先获取到 AppClassLoader
         * jdk.internal.loader.ClassLoaders.AppClassLoader#loadClass(java.lang.String, boolean)
         * -> jdk.internal.loader.BuiltinClassLoader#loadClass(java.lang.String, boolean)
         * -> jdk.internal.loader.BuiltinClassLoader#loadClassOrNull(java.lang.String, boolean)
         * -> jdk.internal.loader.BuiltinClassLoader#findClassInModuleOrNull(jdk.internal.loader.BuiltinClassLoader.LoadedModule, java.lang.String)  由于当前项目为module(jdk),且为第一次加载
         * -> jdk.internal.loader.BuiltinClassLoader#defineClass(java.lang.String, jdk.internal.loader.BuiltinClassLoader.LoadedModule) 在这里会将当前 .class文件转换为 byte[],并查找到 当前.class文件所在的文件夹
         * -> java.lang.ClassLoader#defineClass(java.lang.String, java.nio.ByteBuffer, java.security.ProtectionDomain)
         * -> java.lang.ClassLoader#defineClass(java.lang.String, byte[], int, int, java.security.ProtectionDomain)
         * -> java.lang.ClassLoader#defineClass1(java.lang.ClassLoader, java.lang.String, byte[], int, int, java.security.ProtectionDomain, java.lang.String) (native 调用 jvm)
         *
         */
        ClassLoader.getSystemClassLoader().loadClass("com.xing.level.User");
        /**
         * 如果最终在当前工程中都找不到当前class则会抛出 ClassNotFoundException
         */
//        ClassLoader.getSystemClassLoader().loadClass("com.xing.level.User1");
        /**
         * 这里依赖了 spring-core:5.2.8 并在module-info中添加了 " requires spring.core;"
         */
        // file:/Users/xingguo/.m2/repository/org/springframework/spring-core/5.2.8.RELEASE/spring-core-5.2.8.RELEASE.jar
        // 加载当前jar中的class文件
        ClassLoader.getSystemClassLoader().loadClass("org.springframework.util.CollectionUtils");
    }

    public static void printClassPath(Class<?> klass) {
        CodeSource codeSource = klass.getProtectionDomain().getCodeSource();
        // classLoader 为 null 表示 其是bootstrapClassLoader
        log.info("classLoader is {}, {}'s codeSource is {}", klass.getClassLoader(), klass, codeSource);
    }
}
