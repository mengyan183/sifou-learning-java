/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * FunctionMan
 * 面向过程
 *
 * @author guoxing
 * @date 9/10/20 10:01 AM
 * @since 1.0.0
 */
public class FunctionMain {

    /**
     * @param <S> 来源类型泛型
     * @param <T> 目标类型泛型
     */
    interface Convert<S, T extends Serializable> {
        T convert(S s);
    }


    /*
        对于访问性
        < java9
        public : 全局
        protected : 继承+同包
        (default) : 同包
        private : 当前类
     */
    private static Logger LOGGER = Logger.getLogger("com.xingguo");

    public static void main(String[] args) {
        // java包含的基础数据类型
        // byte(8) char(16) short(16) int(32) long(64)  float/double

        try {
            saveMethod("");
        } catch (Exception exception) {
            // 可以通过手动捕获异常来避免异常继续上抛
            LOGGER.warning(exception.getMessage());
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
        // 对于泛型 编译时处理 以及 运行时擦除的理解
        //
        Convert<Integer, String> convert = new Convert<>() {
            // 编译时处理,当在使用时对泛型使用指定类型,如果声明时的类型和实际使用时的类型不一致,则会直接抛出编译错误
//        Convert<Integer, String> convert = new Convert<String,String>() {
            @Override
            public String convert(Integer integer) {
                return String.valueOf(integer);
            }
        };


        List a = Collections.emptyList();
        List b = Collections.emptyList();
        // true , 对于不包含泛型初始化的list实际都是使用的相同的实例数据
        LOGGER.info(String.valueOf(a == b));

        List<String> a1 = Collections.emptyList();
        boolean v = a == a1;
        // true, 对于 a 和 a1 两个参数的类型实际是不同的,但在运行时实际是对于a和a1的类型实际都是相同的List类型; 可以通过 javap -v 类名 来查看编译后的class文件
        LOGGER.info(String.valueOf(v));

        List<Integer> b1 = Collections.emptyList();
        // 由于a1和b1的泛型的具体类型不一致,因此在编译时不会通过
//        boolean v1 = a1 == b1
        LOGGER.info(a1.getClass().toString());
        LOGGER.info(b1.getClass().toString());
        List<String> arrayList1 = new ArrayList<>();
        List<Integer> arrayList2 = new ArrayList<>();
        LOGGER.info(arrayList1.getClass().toString());
        LOGGER.info(arrayList2.getClass().toString());
    }

    // 方法的名称以及参数的名称; 异常
    // 对于Exception 属于必须人工捕获异常,否则会编译不通过
    private static void saveMethod(String s) throws Exception {
// 对于 非校验性异常 , 非强制要求人工捕获
//    private static void saveMethod(String s) throws RuntimeException {

    }

    private void doConvert(List inputData) {

    }
    // 对于 doConvert 上下两个方法签名在编译后实际都是 doConvert(List),因此都是相同的方法签名,不属于方法重载
//    private void doConvert(List<String> inputData){
//
//    }


}
