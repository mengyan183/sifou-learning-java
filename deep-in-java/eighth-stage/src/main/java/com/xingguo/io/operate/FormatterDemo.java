/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.operate;

import java.util.Formatter;

/**
 * FormatterDemo
 * {@link java.util.Formatter}
 * 格式化工具
 *
 * @author guoxing
 * @date 2020/12/5 4:53 PM
 * @since
 */
public class FormatterDemo {
    public static void main(String[] args) {
        System.out.printf("Hello%s\n", "World");
        System.out.print(String.format("Hello%s\n", "World"));
        // 对于以上实际 都利用到了{@link java.util.Formatter#format}
        System.out.print(new Formatter().format("Hello%s\n", "World"));
    }
}
