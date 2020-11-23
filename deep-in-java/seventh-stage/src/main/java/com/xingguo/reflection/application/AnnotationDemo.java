/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.reflection.application;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

/**
 * AnnotationDemo
 * 注解相关
 *
 * @author guoxing
 * @date 2020/11/20 9:14 AM
 * @since
 */
@Slf4j
public class AnnotationDemo {
    public static void main(String[] args) {
        Class<ToString> toStringClass = ToString.class;
        // 判断当前类型是否为注解类型
        boolean annotation = toStringClass.isAnnotation();
        log.info("当前类型是否为注解类型:{}", annotation ? "是" : "否");
        Class<CustomWithAnnotation> customWithAnnotationClass = CustomWithAnnotation.class;
        // 反射- 注解相关接口
        // 对于在运行时实时获取注解数据要求其生效范围必须为 从前端编译到后端编译整个周期
        ToString toString = customWithAnnotationClass.getAnnotation(ToString.class);
        log.info("当前class:{}是否使用了当前注解:{};结论为:{}", customWithAnnotationClass, toString, Objects.nonNull(toString) ? "是" : "否");

    }


}

/**
 * 对比 不同的RetentionPolicy
 * SOURCE :
 *Constant pool:
 *    #1 = Methodref          #3.#13         // java/lang/Object."<init>":()V
 *    #2 = Class              #14            // com/xingguo/reflection/application/CustomWithAnnotation
 *    #3 = Class              #15            // java/lang/Object
 *    #4 = Utf8               <init>
 *    #5 = Utf8               ()V
 *    #6 = Utf8               Code
 *    #7 = Utf8               LineNumberTable
 *    #8 = Utf8               LocalVariableTable
 *    #9 = Utf8               this
 *   #10 = Utf8               Lcom/xingguo/reflection/application/CustomWithAnnotation;
 *   #11 = Utf8               SourceFile
 *   #12 = Utf8               AnnotationDemo.java
 *   #13 = NameAndType        #4:#5          // "<init>":()V
 *   #14 = Utf8               com/xingguo/reflection/application/CustomWithAnnotation
 *   #15 = Utf8               java/lang/Object
 * CLASS :
 * Constant pool:
 *    #1 = Methodref          #3.#15         // java/lang/Object."<init>":()V
 *    #2 = Class              #16            // com/xingguo/reflection/application/CustomWithAnnotation
 *    #3 = Class              #17            // java/lang/Object
 *    #4 = Utf8               <init>
 *    #5 = Utf8               ()V
 *    #6 = Utf8               Code
 *    #7 = Utf8               LineNumberTable
 *    #8 = Utf8               LocalVariableTable
 *    #9 = Utf8               this
 *   #10 = Utf8               Lcom/xingguo/reflection/application/CustomWithAnnotation;
 *   #11 = Utf8               SourceFile
 *   #12 = Utf8               AnnotationDemo.java
 *   #13 = Utf8               RuntimeInvisibleAnnotations // 重点
 *   #14 = Utf8               Lcom/xingguo/reflection/application/ToString;
 *   #15 = NameAndType        #4:#5          // "<init>":()V
 *   #16 = Utf8               com/xingguo/reflection/application/CustomWithAnnotation
 *   #17 = Utf8               java/lang/Object
 *
 * RUNTIME :
 * Constant pool:
 *    #1 = Methodref          #3.#15         // java/lang/Object."<init>":()V
 *    #2 = Class              #16            // com/xingguo/reflection/application/CustomWithAnnotation
 *    #3 = Class              #17            // java/lang/Object
 *    #4 = Utf8               <init>
 *    #5 = Utf8               ()V
 *    #6 = Utf8               Code
 *    #7 = Utf8               LineNumberTable
 *    #8 = Utf8               LocalVariableTable
 *    #9 = Utf8               this
 *   #10 = Utf8               Lcom/xingguo/reflection/application/CustomWithAnnotation;
 *   #11 = Utf8               SourceFile
 *   #12 = Utf8               AnnotationDemo.java
 *   #13 = Utf8               RuntimeVisibleAnnotations // 重点在这里
 *   #14 = Utf8               Lcom/xingguo/reflection/application/ToString;
 *   #15 = NameAndType        #4:#5          // "<init>":()V
 *   #16 = Utf8               com/xingguo/reflection/application/CustomWithAnnotation
 *   #17 = Utf8               java/lang/Object
 *
 *
 *   对于不同的三者可以看出来
 *      对于 SOURCE在前端编译结束后的class文件中就不包含当前相关注解信息,所以通过直接解析class文件和Class对象都是无法得到相关的数据;
 *      对于 CLASS 和 RUNTIME 只通过分析class文件两者是没有任何区别的; 因此对于该两种是可以通过直接解析class文件得到相关数据
 *      但CLASS和RUNTIME的区别点在于当在运行时解析Class对象时可以看出,只有RUNTIME才能在运行时得到相关的静态数据
 */
@ToString
class CustomWithAnnotation {

}

/**
 * 自定义 toString 注解
 * 使用 javap -v 命令解析
 * interface com.xingguo.reflection.application.ToString extends java.lang.annotation.Annotation
 * minor version: 0
 * major version: 52
 * flags: (0x2600) ACC_INTERFACE, ACC_ABSTRACT, ACC_ANNOTATION
 * <p>
 * 可以看到对于编译后的 ToString 注解实际是
 * "interface com.xingguo.reflection.application.ToString extends java.lang.annotation.Annotation"
 * 但重点还存在一点 flags: (0x2600) ACC_INTERFACE, ACC_ABSTRACT, ACC_ANNOTATION 中的 ACC_ANNOTATION 当前标记位是java编译器提升后的结果,其作用是为了标记当前class 是一个 注解类,如果不存在当前标记在jvm c++解析class文件时就会将当前文件解析为普通的interface
 */

/**
 * 模仿编译后的注解
 * 查看class文件信息
 * interface com.xingguo.reflection.application.CopyToString extends java.lang.annotation.Annotation
 * minor version: 0
 * major version: 52
 * flags: (0x0600) ACC_INTERFACE, ACC_ABSTRACT
 * 在当前的flags中并不会存在 ACC_ANNOTATION标记信息,对于jvm而言,当前类就是一个普通的interface
 */
interface CopyToString extends java.lang.annotation.Annotation {

}

/**
 * 对于Retention 和 Target java.lang.annotation.*中的所有的相关注解实际都称为原生注解
 * 对于Target实际是表示的其使用位置限定
 */
@Target(ElementType.TYPE)
/**
 * 对于 RetentionPolicy 三个周期的范围
 * SOURCE : 在编译为class文件后就被忽略,在class文件中是不包含任何相关信息的
 *  CLASS : 在前端编译阶段存活,意味着在class文件中存在相关的信息,但在被加载到JVM中时进入后端编译阶段,就不会包含相关信息
 * RUNTIME : 从前端编译到后端编译运行阶段都会一直存活
 */
@Retention(RetentionPolicy.SOURCE)
//@Retention(RetentionPolicy.CLASS)
//@Retention(RetentionPolicy.RUNTIME)
// 对于其作用实际是定义了注解使用时的存活周期
@interface ToString {


    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    @interface Exclude {
    }

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    @interface Include {
    }
}

/**
 * TODO : 如何使 自定义注解生效; 动态代理?????
 */
