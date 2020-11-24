/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.introspector;

import com.xingguo.java.beans.properties.Person;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * PersonIntrospector
 * 对于 Person javabean 内省
 * 对于内省的理解为通过反射等 来推导出相应的 属性/事件/方法
 *
 * @author guoxing
 * @date 2020/11/24 4:57 PM
 * @since
 */
@Slf4j
public class PersonIntrospector {
    public static void main(String[] args) throws IntrospectionException, InterruptedException {
        // 获取 bean 信息
        // 对于 第一个参数表示要 内省的对象,第二个参数表示 中止的对象
        BeanInfo beanInfo = Introspector.getBeanInfo(Person.class, Object.class);
//        BeanInfo beanInfo = Introspector.getBeanInfo(Person.class);
        // 描述符
        // 对于 java.beans.BeanDescriptor.beanClassRef (当前类的引用)和 java.beans.BeanDescriptor.customizerClassRef(当前类的自定义类引用) 存储的数据 实际都是weakreference , 对于 weakReference 的只要在gc发生时就会被回收
        // 对于 使用 weakReference的优势点就在于当gc发生时不会由于当前数据的引用存在导致当前对象gc失败
        BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
        log.info("{}", beanDescriptor);

        log.info("======================================");
        // 属性描述符
        // 当在生成beanInfo中不排除Object.class时,在属性描述中其会推导出 class 属性,原因是存在 java.lang.Object.getClass 方法
        // 且其中包含 bound 和 constrained 属性描述信息
        // 对于bound 只要存在 properties存在 propertyChange 就认为其存在 bound
        // 而对于constrained 则不光要求 setter 方法 返回值为void 且 要求必须显式抛出 propertyChangeException
        // 对于 write 和 set 方法的判断则根据 get和set prefix 判断
        Stream.of(beanInfo.getPropertyDescriptors())
                .forEach(propertyDescriptor -> {
                    log.info("{}", propertyDescriptor);
                });

        log.info("======================================");
        // 方法描述符
        Stream.of(beanInfo.getMethodDescriptors())
                .forEach(methodDescriptor -> {
                    log.info("{}", methodDescriptor);
                    // 对于 parameterDescriptors 字段 除非手动调用创建,否则获取到的永远都为空
                    if (Objects.nonNull(methodDescriptor.getParameterDescriptors())) {
                        Stream.of(methodDescriptor.getParameterDescriptors())
                                .forEach(parameterDescriptor -> {
                                    log.info("{}", parameterDescriptor);
                                });
                    }
                    // 对于 params / paramNames 等私有属性,并不存在public 方法支持访问
                });
        log.info("======================================");
        // 事件描述符
        Stream.of(beanInfo.getEventSetDescriptors())
                .forEach(eventSetDescriptor -> {
                    log.info("{}", eventSetDescriptor);
                });
    }
}
