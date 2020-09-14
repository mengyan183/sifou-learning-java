/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional.annocation;

/**
 * CustomAnnotationDemo
 *
 * @author guoxing
 * @date 9/14/20 5:50 PM
 * @since
 */
@CustomAnnotation(value = "test")
public class CustomAnnotationDemo {
    public static void main(String[] args) {
        // 代理实际也是字节码提升的一种
        // 这里实际是通过sun.reflect.annotation.AnnotationInvocationHandler 生成了代理方法
        CustomAnnotation annotation = CustomAnnotationDemo.class.getAnnotation(CustomAnnotation.class);
        // 这里实际是调用了sun.reflect.annotation.AnnotationInvocationHandler.invoke 来获取当前value中的数据
        System.out.println(annotation.value());
    }
}
