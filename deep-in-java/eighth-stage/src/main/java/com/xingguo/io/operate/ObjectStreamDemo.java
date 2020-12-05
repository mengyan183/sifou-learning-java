/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.operate;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ObjectStreamDemo
 * {@link java.io.ObjectOutputStream}
 * {@link java.io.ObjectInputStream}
 * 一般利用此操作支持对象序列化操作
 *
 * @author guoxing
 * @date 2020/12/5 7:16 PM
 * @since
 */
public class ObjectStreamDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 以 {@link java.util.ArrayList}为例
         * {@link java.io.Externalizable}
         * {@link java.io.Serializable}
         */
        ArrayList<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        File file = new File("integers.ser");
        // 序列化
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            // 对于当前写入方法,在执行时会判断当前序列化数据类型是否实现了{@link java.io.Serializable}如果实现了,则根据反射判断当前类是否存在writeObject方法,如果存在对应的方法,则利用已存在的方法执行序列化操作
            /**
             * 对于 {@link java.util.ArrayList} 序列化操作, 对于 {@link ArrayList#elementData} 使用了 transient,因此在执行序列化操作时当前字段不会被直接序列化存储
             * 但在 {@link ArrayList#writeObject(java.io.ObjectOutputStream)}自定义序列化操作时,其并不直接序列化操作Objet[] ,如果直接序列化当前字段需要保存多个数据,首先是Object类型,其次是数组类型包含长度等数据,最后才是数组中的每个元素; 对于以上的操作实际会浪费一些序列化的性能,因此当前操作优化了当前操作,对于 {@link ArrayList#elementData}数据的序列化操作其首先储存数组的长度,然后循环解析数组中的每个元素并保存
             */
            objectOutputStream.writeObject(integers);
            objectOutputStream.flush();
        }

        // 反序列化
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
            /**
             * 在反序列化操作时实际是利用的{@link ArrayList#readObject(java.io.ObjectInputStream)}
             * 其首先根据序列化文件中保存了类描述信息,以及对象的数据信息,先将其反序列化为ArrayList对象,此时对于{@link ArrayList#elementData} 已初始化完成,但数组中具体的元素还尚未完全反序列化,此时通过直接反序列化(类似于深度copy)每个元素,将其写入到数组中,实现完全的反序列化
             *
             * 根据此操作可以看出对于ArrayList在序列化和反序列化操作的简单优化,因此如果我们自己需要自定义序列化操作则可以通过直接实现{@link Externalizable}接口来实现 readObject和writeObject的操作
             */
            List<Integer> list = (List) objectInputStream.readObject();
            list.forEach(System.out::println);
        }
        // 删除文件
        file.delete();


    }
}
