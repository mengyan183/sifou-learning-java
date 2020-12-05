/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.characterstream;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;

/**
 * StringReaderDemo
 *
 * @author guoxing
 * @date 2020/12/5 2:26 PM
 * @since
 */
public class StringReaderDemo {
    public static void main(String[] args) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        String path = Objects.requireNonNull(contextClassLoader.getResource("")).getPath();
        StringReader stringReader = new StringReader(path);
        StringWriter stringWriter = new StringWriter(1024);
//        while (stringReader.read()){
//            stringWriter.append()
//        }
    }
}
