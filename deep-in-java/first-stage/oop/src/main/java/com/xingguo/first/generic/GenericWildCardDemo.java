/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * GenericWildCardDemo
 *
 * @author guoxing
 * @date 9/13/20 3:18 PM
 * @since
 */
public class GenericWildCardDemo {

    public static void main(String[] args) {
        List<Number> numbers = new ArrayList<>();
        // 边界上限限定符 extends
        upperBoundedWildcards(numbers);
        // 无界操作
        unBoundedWildcards(numbers); // 当参数为 List<Object>时,会抛出编译错误
        // 边界下限限定符 super
        lowerBoundedWildcards(numbers);
        lowerBoundedWildcards(Arrays.asList(new Object()));
    }

    // 消费数据,要求下限; CS consumer use super ;
    private static void lowerBoundedWildcards(List<? super Number> numbers) {
        // 操作参数时要求 super
        numbers.add(Integer.valueOf("1"));
    }

//    private static void unBoundedWildcards(List<Object> numbers) { // 当这里设置为指定类型Object 时,就需要进行类型约束; 且由于泛型类型擦除的问题,当前方法签名和下面方法签名一致,所以也会抛出编译错误
//    }

    private static void unBoundedWildcards(List<?> numbers) {
        // 对于当前通配符为 ? 表示无界限, 虽然在运行时进行了类型擦除,为object类型;但和Object类型实际并不相等
        // Object表示了是一个指定的类型
    }

    // producer use extends
    public static void upperBoundedWildcards(List<Number> numbers) {
        // 泛型上界通配符
        // 对于 Number 的子集 包含Byte /Short /Integer 等类型
        numbers.add(Byte.valueOf("1"));
        numbers.add(Short.valueOf("2"));
        numbers.add(Integer.valueOf("3"));

        List<Byte> bytes = Arrays.asList(Byte.valueOf("4"));
        List<Short> shorts = Arrays.asList(Short.valueOf("5"));
        List<Integer> integers = Arrays.asList(Integer.valueOf("6"));
        // 被操作对象可以为更加抽象的类型(numbers)
        // 实际输入数据要求为更具体的类型(bytes/shorts)
        numbers.addAll(integers);
        numbers.addAll(bytes);
        // 对于addAll 中的参数 实际是读取操作,因此 满足PE producer use extends
        numbers.addAll(shorts);

        upperBoundedWildcardsForEach(numbers, System.out::println);
    }

    // PECS
    public static void upperBoundedWildcardsForEach(List<? extends Number> numbers, Consumer<? super Number> consumer) {
        numbers.forEach(consumer);
    }

    private static void producerExtendsConsumerSuperDemo(List<? extends Number> producer, List<? super Number> consumer) {
        // PECS stands for producer-extends, consumer-super.
        // 读取数据（生产者）使用 extends
        for (Number number : producer) {
            System.out.println(number);
        }
        // 操作输出（消费者）使用 super
        consumer.add(1);
        consumer.add((short) 2);
    }
}
