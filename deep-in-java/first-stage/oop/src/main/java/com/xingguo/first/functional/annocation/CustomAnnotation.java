/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional.annocation;

import java.lang.annotation.*;
// 通过对当前注解类的class文件使用javap -v 可以看到 当前注解实际是一个接口继承了
//public interface com.xingguo.first.functional.annocation.CustomAnnotation extends java.lang.annotation.Annotation
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CustomAnnotation {
     String value();
}
