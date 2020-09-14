/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.functional;

/**
 * FunctionalInterfaceDemo
 * 函数式设计
 *
 * @author guoxing
 * @date 9/14/20 9:41 AM
 * @since
 */
public class FunctionalInterfaceDemo {

    public static void main(String[] args) {
        Function1 function1 = ()->{
            return "function1";
        };
        // 接口可以直接调用Object中的public方法
        System.out.println(function1.toString());
        System.out.println(function1.getName());
        FunctionInterfaceWithoutAnnotation functionInterfaceWithoutAnnotation = ()->{
            return "functionInterfaceWithoutAnnotation";
        }; // 当接口中存在多个抽象方法时,该操作会编译报错(Multiple non-overriding abstract methods found in interface),原因在于无法找到一个唯一的抽象方法
        System.out.println(functionInterfaceWithoutAnnotation.getClassName());
        System.out.println(functionInterfaceWithoutAnnotation.getName());
    }


    @FunctionalInterface
    public interface Function1 {
        String getName();
    }

    // 接口中有且只能有一个抽象方法(排除default/以及Object中的public)
    public interface FunctionInterfaceWithoutAnnotation {
        String getName();

        // 当存在多个抽象方法时,当前接口就不是函数式接口
//        String getName1();

        default String getClassName() {
            return this.getClass().getName();
        }
    }

//    @FunctionalInterface // 当注解使用在类上,会编译不通过; 该注解只能用在接口上
//    public static class A{
//
//    }
}
