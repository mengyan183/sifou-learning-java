/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.reflection;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generic
 *
 * @author guoxing
 * @date 2020/11/11 1:41 PM
 * @since
 */
@Slf4j
public class Generic<E> {
    private List<E> list;

    public Generic() {
        this.list = getList();
    }

    public List<E> getList() {
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        Generic<Integer> integerGeneric = new Generic<>();
        Class<? extends Generic> aClass = integerGeneric.getClass();
        Type genericSuperclass = aClass.getGenericSuperclass();
        log.info("{}", genericSuperclass);
        TypeVariable<Class<Generic>>[] typeParameters = Generic.class.getTypeParameters();
        Arrays.stream(typeParameters).forEach(t -> {
            log.info("{}", t);
        });
        Arrays.stream(aClass.getTypeParameters()).forEach(t -> {
            log.info("{}", t);
        });

        List<Integer> integers = new ArrayList<>();
        Arrays.stream(integers.getClass().getGenericInterfaces())
                .forEach(t -> {
                    if (t instanceof ParameterizedType) {
                        Type[] actualTypeArguments = ((ParameterizedType) t).getActualTypeArguments();
                        Arrays.stream(actualTypeArguments).forEach(gt -> {
                            log.info("{}", gt.getTypeName());
                        });
                    }
                });
        Type genericSuperclass1 = integers.getClass().getGenericSuperclass();
        if (genericSuperclass1 instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass1;
            String typeName = parameterizedType.getRawType().getTypeName();
            log.info("{}", typeName);
        }

        log.info("{}", aClass.getGenericSuperclass().toString());

        /**
         * 泛型擦写指代的是运行时擦写,对于运行时存储的数据jvm都认为是Object对象,而非具体的类型
         * 对于 类似 class CustomStringList extends ArrayList<String> 这种定义方式,在定义类时就已经确定了具体的泛型,因此对于相应的定义在编译为class文件时就已经保存在文件中了
         */
        String typeName = CustomStringList.class.getGenericSuperclass().getTypeName();
        log.info("{}", typeName);// java.util.ArrayList<java.lang.String>
    }
}

// 当对一个类执行具体化泛型时
class CustomStringList extends ArrayList<String> {

}
