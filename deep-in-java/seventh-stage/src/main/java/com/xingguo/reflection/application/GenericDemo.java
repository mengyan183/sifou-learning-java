/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.reflection.application;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * GenericDemo
 * 反射泛型相关
 *
 * @author guoxing
 * @date 2020/11/17 8:56 PM
 * @since
 */
@Slf4j
public class GenericDemo {

    public static void main(String[] args) throws URISyntaxException {
        // 解析当前目录下使用@Repository注解的实现类
        // 获取当前class所在路径
        URL location = GenericDemo.class.getProtectionDomain().getCodeSource().getLocation();
        // 获取当前前端编译输出路径
        String dependencyPath = location.getPath();
        // 获取当前类的包路径
        String packageName = GenericDemo.class.getPackage().getName();
        // 组合完整的文件路径
        String folder = dependencyPath.concat(packageName.replace(".", File.separator));
        // 扫描当前系统下的class文件
        File file = new File(folder);
        if (file.isDirectory()) {
            // 过滤当前文件夹下的全部class文件名称
            String[] classNames = file.list((dir, name) -> name.endsWith(".class"));
            if (Objects.isNull(classNames)) {
                return;
            }
            // 获取当前线程类加载器
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            // 存储使用Repository注解的类
            List<Class<?>> repositoryAnnotationClass = new LinkedList<>();
            // 加载当前Class
            Arrays.stream(classNames)
                    // 转换当前className为全路径
                    .map(className -> packageName.concat(".").concat(className.substring(0, className.lastIndexOf("."))))
                    .forEach(reference -> {
                        // 加载class并进行筛选
                        try {
                            Class<?> aClass = contextClassLoader.loadClass(reference);
                            // 判断当前类是否使用@Repository
                            if (aClass.isAnnotationPresent(Repository.class)) {
                                repositoryAnnotationClass.add(aClass);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
            // 解析当前类
            repositoryAnnotationClass.stream()
                    .filter(c ->
                            // 获取当前类全部实现的接口
                            !Modifier.isAbstract(c.getModifiers()) && UserRepository.class.isAssignableFrom(c)// 判断当前类是否实现了当前接口
                    ).forEach(c -> {
                // 解析当前类的接口中的泛型字段的具体类型
                Arrays.stream(c.getGenericInterfaces())
                        .filter(type -> type instanceof ParameterizedType) // 过滤当前接口类型为执行泛型的接口
                        .map(type -> (ParameterizedType) type) // 将当前类型转换为ParameterizedType
                        .forEach(parameterizedType -> {
                            if (UserRepository.class.equals(parameterizedType.getRawType())) {
                                // 获取当前泛型类型的泛型参数("<>"中的实际定义)
                                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                                // 遍历当前泛型定义的参数
                                // 对于当前数据的作用
                                // 类似于 spring-data 中的 "org.springframework.data.repository.CrudRepository<T, ID>" 根据 泛型 <T> 来实现动态sql生成
                                Stream.of(actualTypeArguments)
                                        .forEach(type -> {
                                            String typeName = type.getTypeName();
                                            log.info("{}", typeName);
                                            try {
                                                contextClassLoader.loadClass(typeName);
                                            } catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        });
                            }
                        });

            });
        }

    }
}


// 参考spring-data repository

interface UserRepository<T> {

}

/**
 * 实际执行
 */
@Repository(tableName = "user")
class UserRepositoryImpl implements UserRepository<User>,
        Comparable<UserRepositoryImpl>,
        Serializable {
    @Override
    public int compareTo(UserRepositoryImpl o) {
        return 0;
    }
}

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@interface Repository {
    String tableName() default "";
}

class User {
    private String name;

    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}