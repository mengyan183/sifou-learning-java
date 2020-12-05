/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.characterstream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * InputStreamToReaderDemo
 *
 * @author guoxing
 * @date 2020/12/5 2:33 PM
 * @since
 */
public class InputStreamToReaderDemo {

    public static void main(String[] args) {
        InputStream in = System.in;
        // 适配器模式
        // 对于适配器模式 (IN 类型,OUT类型), 需要注入IN类型数据,并输出OUT类型数据
        // 适配对象属于OUT类型数据,注入的是IN类型数据,且对于OUT和IN类型没有任何层次关系
        InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);

    }
}
