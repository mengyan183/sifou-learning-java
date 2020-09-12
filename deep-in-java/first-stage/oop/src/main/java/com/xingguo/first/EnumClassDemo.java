/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

/**
 * EnumClassDemo
 * 枚举类
 *
 * @author guoxing
 * @date 9/12/20 4:43 PM
 * @since
 */
public class EnumClassDemo {
    public static void main(String[] args) {
        //问题1:如何得知当前常量定义的顺序?
        //问题2:能否输出所有的成员
        printLn(Counting.ONE);
        printLn(Counting.FOUR);
        // 问题3:是否可以输出全部定义
        printAllCountingValues();

        // 对于枚举的tostring方法实际是输出的当前枚举定义名称
        printLn(CountingEnum.ONE);
        printLn(CountingEnum.FIVE);
        printAllEnumValues();
        printEnumMetaData(CountingEnum.FIVE);
    }

    // 输出当前枚举类
    private static void printLn(Counting counting) {
        System.out.println(counting);
    }


    // 输出当前枚举
//    private static void printLn(CountingEnum countingEnum) {
    // 对于枚举实际是默认继承了java.lang.Enum
    private static void printLn(Enum countingEnum) {
        System.out.println(countingEnum);
    }

    // 输出枚举中的全部字段定义
    private static void printAllEnumValues() {
        // 对于当前枚举调用的values方法实际java字节码提升自动生成的values方法
        Stream.of(CountingEnum.values())
                .forEach(System.out::println);
    }

    //数据枚举中的相关元信息, 这些元信息都来源于 java.lang.Enum定义,且都是不可变的
    private static void printEnumMetaData(CountingEnum countingEnum) {
        System.out.println("定义序号:" + countingEnum.ordinal());
        System.out.println("枚举名称:" + countingEnum.name());
    }

    // 输出Counting中的全部常量定义
    private static void printAllCountingValues() {
        Stream.of(Counting.values())
                .forEach(System.out::println);
    }

}

/**
 * 查看编译后的枚举javap CountingEnum.class
 * <p>
 * final class com.xingguo.first.CountingEnum extends java.lang.Enum<com.xingguo.first.CountingEnum> {
 * public static final com.xingguo.first.CountingEnum ONE;
 * public static final com.xingguo.first.CountingEnum TWO;
 * public static final com.xingguo.first.CountingEnum THREE;
 * public static final com.xingguo.first.CountingEnum FOUR;
 * public static final com.xingguo.first.CountingEnum FIVE;
 * public static com.xingguo.first.CountingEnum[] values();
 * public static com.xingguo.first.CountingEnum valueOf(java.lang.String);
 * static {};
 * }
 */
enum CountingEnum /*extends SnapshotDemo.Data*/ implements Serializable { // 不可被显式继承和被继承,但支持实现接口
    ONE(1) {
        // 抽象方法实现,可以参考 TimeUnit
        @Override
        public void valueAsString() {
            System.out.println(getValue());
        }
    },
    TWO(2) {
        @Override
        public void valueAsString() {
            System.out.println(getValue());
        }
    },
    THREE(3) {
        @Override
        public void valueAsString() {
            System.out.println(getValue());
        }
    },
    FOUR(4) {
        @Override
        public void valueAsString() {
            System.out.println(getValue());
        }
    },
    FIVE(5) {
        @Override
        public void valueAsString() {
            System.out.println(getValue());
        }
    };
    private final int value;

    public int getValue() {
        return value;
    }

    /*private*/ CountingEnum(int value) {
        this.value = value;
    }

    // 抽象方法定义
    public abstract void valueAsString();
}
// 枚举不可被显式继承
//class ExtendEnum extends CountingEnum {
//    public ExtendEnum() {
//        super();
//    }
//}

// 枚举类 ;
// 使用 final 修饰表示不可被继承修改
// 缺陷:
// 有强类型约束,相对于普通的常量而言,当前枚举类中的常量只能为当前类型
/*final*/ abstract class Counting extends SnapshotDemo.Data {
    // 声明常量
    public static final Counting ONE = new Counting(1) {
        @Override
        public void valueAsString() {
            System.out.println(this.getValue());
        }
    };
    public static final Counting TWO = new Counting(2) {
        @Override
        public void valueAsString() {
            System.out.println(this.getValue());
        }
    };
    public static final Counting THREE = new Counting(3) {
        @Override
        public void valueAsString() {
            System.out.println(this.getValue());
        }
    };
    public static final Counting FOUR = new Counting(4) {
        @Override
        public void valueAsString() {
            System.out.println(this.getValue());
        }
    };

    private int value;

    public int getValue() {
        return value;
    }

    // 私有构造方法
    private Counting(int value) {
        this.value = value;
    }

    public abstract void valueAsString();

    @Override
    public String toString() {
        return "Counting{" +
                "value=" + value +
                '}';
    }

    // 通过反射的方式实现类似于java 字节码提升的values方法
    public static Counting[] values() {
        // 通过反射获取当前全部常量字段
        return Stream.of(Counting.class.getDeclaredFields()) // 获取全部声明的字段
                .filter(field -> {
                    // 获取当前字段定义的修饰符; 对于常量而言 全部都是 public static final
                    int modifiers = field.getModifiers();
                    return Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && Modifier.isFinal(modifiers);
                })
                .map(field -> {
                    // 将当前反射得到的字段再转换为实际数据
                    try {
                        return field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).toArray(Counting[]::new);
    }
}
