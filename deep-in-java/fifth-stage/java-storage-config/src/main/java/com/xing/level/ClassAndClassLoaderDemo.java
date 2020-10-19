/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xing.level;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassAndClassLoaderDemo
 *
 * @author guoxing
 * @date 2020/10/16 4:42 PM
 * @since
 */
@Slf4j
public class ClassAndClassLoaderDemo {
    public static void main(String[] args) throws ClassNotFoundException {
//        compareClassLoader();
//        primitiveVsObject();
//        loadClass();
        //对于 jdk定义的相关class 都是由 根classLoader(bootstrapClassLoader) jvm进行直接加载
        printClassLoader(Object.class);
        printClassLoader(int.class);
        // 对于 自定义 class 则一般为jdk.internal.loader.ClassLoaders$AppClassLoader(应用级别)
        printClassLoader(User.class);
    }

    /**
     * 获取指定class的ClassLoader
     *
     * @param klass
     */
    public static void printClassLoader(Class klass) {
        ClassLoader classLoader = klass.getClassLoader();
        log.info("{}的classLoader为 {}", klass, classLoader == null ? "BootStrapClassLoader" : classLoader);
    }

    /**
     * 原生类型和对象类型比较
     * 对于 是否为原生类型 可以 通过 Class#isprimitive(native function)来判断
     */
    public static void primitiveVsObject() {
        // primitive class
        Class<Integer> integerClass = int.class;
        boolean primitive = integerClass.isPrimitive();
        assert primitive;
    }

    public static void loadClass() throws ClassNotFoundException {

        /**
         *在 classLoader 类 加载时 就会执行静态代码块中的代码
         * 从而成功创建了
         *     private static final BootClassLoader BOOT_LOADER;
         *     private static final PlatformClassLoader PLATFORM_LOADER;
         *     private static final AppClassLoader APP_LOADER;
         */
        ClassLoader.getPlatformClassLoader().loadClass("com.xing.level.User");
    }

    public static void compareClassLoader() {
        // jdk15 jdk.internal.loader.ClassLoaders$AppClassLoader
        // 系统级 加载器
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        printParentClassLoader(systemClassLoader);
        // 应用 加载器
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        log.info("{}", contextClassLoader);
        // 对于线程的应用加载器可以设置自定义加载器来替换线程中的类加载器
        Thread.currentThread().setContextClassLoader(systemClassLoader);
    }

    public static void printParentClassLoader(ClassLoader classLoader) {
        /**
         * Returns the parent class loader for delegation. Some implementations may use {@code null} to represent the bootstrap class loader.
         */
        // 当parent为 null 时,实际为 bootStrapClassLoader , 对于 bootStrapClassLoader在java中的表现形式为null
        if (classLoader == null || classLoader.getParent() == null) {
            log.info("{} is final parent", classLoader);
            return;
        }
        ClassLoader parent = classLoader.getParent();
        log.info("{} parent -> {}", classLoader, parent);
        printParentClassLoader(parent);
    }

    public static void changeClassLoader(ClassLoader classLoader) {
        Thread currentThread = Thread.currentThread();
        ClassLoader previousContextClassLoader = currentThread.getContextClassLoader();
        try {
            /**
             * 对于一些程序需要兼容不同版本的类加载机制因此需要一些指定的类加载器来加载指定的class
             */
            currentThread.setContextClassLoader(classLoader);
            // 执行相关的类加载操作
        } catch (Exception e) {

        } finally {
            currentThread.setContextClassLoader(previousContextClassLoader);
        }

    }
}
