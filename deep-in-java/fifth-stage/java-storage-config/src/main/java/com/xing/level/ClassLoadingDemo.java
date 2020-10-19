/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xing.level;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * ClassLoadingDemo
 * 类加载过程
 *
 * @author guoxing
 * @date 2020/10/17 9:01 AM
 * @since
 */
@Slf4j
public class ClassLoadingDemo {
    public static void main(String[] args) throws ClassNotFoundException {
        // 加载 user.class文件
        String classPath = System.getProperty("user.dir").concat("/deep-in-java/fifth-stage/java-storage-config/target/classes/");
        log.info("{}", classPath);
        // 对于 User.class 当前操作实际也会将User进行加载, 因此替换为 手动路径
        String classReferenceName = "com.xing.level.User";
        log.info("{}", classReferenceName);
        loadClassBeforeParent(classPath, classReferenceName);
//        loadClassAfterParent(classPath, classReferenceName);

    }

    /**
     * 这里可以体现出双亲委派的特点:
     * 对于classLoader 而言,在执行loadClass时,会首先判断当前classLoader是否已加载过当前class;如果未加载,则首先判断是否有parentClassLoader,如果存在,则会递归调用parent loadClass 如果当前class为首次加载则最终会执行到findBootstrapClass使用bootstrapClassLoader执行class 加载
     *
     * @param classPath
     * @param classReferenceName
     * @throws ClassNotFoundException
     */
    private static void loadClassAfterParent(String classPath, String classReferenceName) throws ClassNotFoundException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        // 父加载器加载 class
        Class<?> aClass1 = contextClassLoader.loadClass(classReferenceName);
        MyClassLoader myClassLoader = new MyClassLoader();
//        Class<?> aClass = myClassLoader.defineClass(classReferenceName, new File(classPath.concat(classReferenceName.replaceAll("\\.", "/").concat(".class"))));
        Class<?> aClass = myClassLoader.loadClass(classReferenceName);
        log.info("{}", aClass == aClass1);
        log.info("{} load {} equals {} load {};{}; \n{}'s parent is {} that equals {};{} ", contextClassLoader, aClass1, myClassLoader, aClass, aClass == aClass1, myClassLoader, myClassLoader.getParent(), contextClassLoader, myClassLoader.getParent() == contextClassLoader);
        myClassLoader.loadClass(classReferenceName);
    }

    /**
     * 在父加载器之前加载class
     *
     * @param classPath
     * @param classReferenceName
     * @throws ClassNotFoundException
     */
    private static void loadClassBeforeParent(String classPath, String classReferenceName) throws ClassNotFoundException {
        MyClassLoader myClassLoader = new MyClassLoader();
        Class<?> aClass2 = myClassLoader.loadClass(classReferenceName);
        Class<?> defineClass = myClassLoader.defineClass(classReferenceName, new File(classPath.concat(classReferenceName.replaceAll("\\.", "/").concat(".class"))));
        log.info("{}'s classLoader :{}; aClass2 == defineClass :{}", defineClass, defineClass.getClassLoader(), aClass2 == defineClass);
        Stream.of(defineClass.getDeclaredFields()).forEach(field -> {
            log.info("字段:{}", field);
        });

        // 获取线程上下文的classLoader
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Class<?> aClass = contextClassLoader.loadClass(classReferenceName);
        log.info("{}", aClass == aClass2);
        /**
         * 对于 myClassLoader 是将当前线程的classLoader作为parent
         * 对于相同的class由于是使用用的不同的ClassLoader进行加载,因此就会存在 User.class != User.class的情况
         */
        log.info("{}是否等于{}加载的相同的class:{}", myClassLoader, contextClassLoader, defineClass == aClass);
        Thread.currentThread().setContextClassLoader(myClassLoader);
        // 重新设置当前线程的classLoader
        Class<?> aClass1 = Thread.currentThread().getContextClassLoader().loadClass(classReferenceName);
        // 根据结果可以得到,对于相同的classLoader 如果已经加载过的class,则不会再次重新创建新的class对象
        // 最终调用的为 "native Class<?> findLoadedClass0(String name)"
        log.info("当重新设置当前线程的classLoader为myclassLoader时:{}", defineClass == aClass1);
    }

    static class MyClassLoader extends ClassLoader {

        /**
         * 指定当前自定义classLoader的parent
         */
        public MyClassLoader() {
            super(Thread.currentThread().getContextClassLoader());
        }

        /**
         * 加载一个.class文件变为class对象
         *
         * @param name
         * @param classFile
         * @return
         */
        public Class<?> defineClass(String name, File classFile) {
            byte[] classBytes = fileToBytes(classFile);
            return super.defineClass(name, classBytes, 0, classBytes.length);
        }

        private byte[] fileToBytes(File classFile) {
            byte[] bytes = null;
            try {
                bytes = FileUtils.readFileToByteArray(classFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return bytes;
        }
    }
}
