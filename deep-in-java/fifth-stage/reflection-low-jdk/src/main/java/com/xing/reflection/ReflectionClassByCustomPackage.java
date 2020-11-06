/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xing.reflection;


import lombok.NonNull;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ReflectionClassByCustomPackage
 * 根据指定的包名加载当前包下的所有的类
 *
 * @author guoxing
 * @date 2020/11/3 7:40 PM
 * @since
 */
@CustomAnnotation
public class ReflectionClassByCustomPackage {
    List<String> classPaths = new ArrayList<>();

    public List<Class<?>> searchClass(String basePack) throws ClassNotFoundException {
        ArrayList<Class<?>> objects = new ArrayList<>();
        //先把包名转换为路径,首先得到项目的classpath
        String classpath = Optional.ofNullable(ReflectionClassByCustomPackage.class.getResource("/")).get().getPath();
        //然后把我们的包名basPach转换为路径名
        basePack = basePack.replace(".", File.separator);
        //然后把classpath和basePack合并
        String searchPath = classpath + basePack;
        doPath(new File(searchPath));
        //这个时候我们已经得到了指定包下所有的类的绝对路径了。我们现在利用这些绝对路径和java的反射机制得到他们的类对象
        for (String s : classPaths) {
            s = s.replace(classpath, "").replace(File.separator, ".").replace(".class", "");
            Class<?> cls = Class.forName(s);
            objects.add(cls);
        }
        return objects;
    }

    public List<Class<?>> getTypesAnnotatedWith(@NonNull final Class<? extends Annotation> annotation, final List<Class<?>> classes) {
        Retention retention = annotation.getAnnotation(Retention.class);
        // 要求注解必须为运行时
        if (Objects.isNull(retention) || !RetentionPolicy.RUNTIME.equals(retention.value())) {
            return classes;
        }
        return classes.stream().filter(c -> Objects.nonNull(c.getAnnotation(annotation))).collect(Collectors.toList());
    }

    /**
     * 该方法会得到所有的类，将类的绝对路径写入到classPaths中
     *
     * @param file
     */
    private void doPath(File file) {
        if (file.isDirectory()) {//文件夹
            //文件夹我们就递归
            File[] files = file.listFiles();
            assert files != null;
            for (File f1 : files) {
                doPath(f1);
            }
        } else {//标准文件
            //标准文件我们就判断是否是class文件
            if (file.getName().endsWith(".class")) {
                //如果是class文件我们就放入我们的集合中。
                classPaths.add(file.getPath());
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        //包名
        String basePack = "com.xing";
        ReflectionClassByCustomPackage reflectionClassByCustomPackage = new ReflectionClassByCustomPackage();
        List<Class<?>> classes = reflectionClassByCustomPackage.searchClass(basePack);
        List<Class<?>> typesAnnotatedWith = reflectionClassByCustomPackage.getTypesAnnotatedWith(CustomAnnotation.class, classes);
        System.out.println(typesAnnotatedWith);
    }
}
