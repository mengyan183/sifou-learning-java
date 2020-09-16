/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection;

import org.springframework.util.StreamUtils;

import java.io.*;
import java.util.ArrayList;

/**
 * CloneDemo
 * 复制
 *
 * @author guoxing
 * @date 9/16/20 4:13 PM
 * @since
 */
public class CloneDemo {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("A");
        // 浅拷贝
        ArrayList<String> shallowCopy = (ArrayList<String>) strings.clone();
        System.out.println(shallowCopy == strings);
        System.out.println(shallowCopy.equals(strings)); // 对于equals的方法当集合对象不相等时,则会使用equals对比集合中的元素
        ArrayList<String> deepCopy = deepClone(strings);
        System.out.println(deepCopy == strings);
        System.out.println(deepCopy.equals(strings)); // 由于String的equals方法是对比的字节数组中的数据,虽然字符串的对象都不相同

        ArrayList<String> deepCloneInSerialization = deepCloneInSerialization(strings);
        System.out.println(deepCloneInSerialization == strings);
        System.out.println(deepCloneInSerialization.equals(strings));
        int size = strings.size();
        for (int i = 0; i < size; i++) {
            String s2 = shallowCopy.get(i);
            String s = deepCopy.get(i);
            String s1 = strings.get(i);
            String s3 = deepCloneInSerialization.get(i);
            assert s != s1;
            assert s2 == s1;
            assert s3 != s1;
            // 创建的的对象实现深拷贝
            System.out.println(s == s1);//false
            // ArrayList中的默认浅拷贝
            System.out.println(s2 == s1);//true
            // 使用序列化方式实现的
            System.out.println(s3 == s1);//false
        }


    }

    /**
     * 采用序列化的操作,同样支持深拷贝
     *
     * @param arrayList
     * @return
     */
    private static ArrayList<String> deepCloneInSerialization(ArrayList<String> arrayList) throws IOException, ClassNotFoundException {
        // 操作快照数据,不要在源数据上直接操作
        ArrayList<String> ts = new ArrayList<>(arrayList);
        // 将数据转换为内存中的输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(ts);
        // 从输出流中读取数据转换为输入流
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        // 使用spring 流拷贝工具类
        StreamUtils.copy(objectInputStream, objectOutputStream);
        return (ArrayList<String>) objectInputStream.readObject();
    }


    private static ArrayList<String> deepClone(ArrayList<String> arrayList) {
        ArrayList<String> ts = new ArrayList<>();
        arrayList.forEach(s -> {
            ts.add(new String(s));
        });
        return ts;
    }
}
