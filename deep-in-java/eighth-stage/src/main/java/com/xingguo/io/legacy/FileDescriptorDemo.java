/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.legacy;

import java.io.FileDescriptor;
import java.lang.reflect.Field;

/**
 * FileDescriptorDemo
 * 文件描述符
 * {@link java.io.FileDescriptor}
 *
 * @author guoxing
 * @date 2020/12/6 2:04 PM
 * @since
 */
public class FileDescriptorDemo {


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        FileDescriptor in = FileDescriptor.in;
        printField(in, "fd");
        printField(in, "parent");
        printField(in, "otherParents");
        printField(in, "closed");
    }

    private static void printField(FileDescriptor in, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<? extends FileDescriptor> aClass = in.getClass();
        Field field = aClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        Object o = field.get(in);
        System.out.println(o);
    }
}
