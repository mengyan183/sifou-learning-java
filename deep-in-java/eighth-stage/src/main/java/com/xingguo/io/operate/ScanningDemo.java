/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.operate;

import java.io.StringReader;
import java.util.Scanner;

/**
 * ScanningDemo
 * {@link java.util.Scanner}
 *
 * @author guoxing
 * @date 2020/12/5 4:41 PM
 * @since
 */
public class ScanningDemo {
    public static void main(String[] args) {
        StringReader stringReader = new StringReader("1,2,3,4,5,6");
        // 对于 scanner默认分割符为 "\n"
        Scanner scanner = new Scanner(stringReader);
        // 指定读取分割符
        scanner.useDelimiter(",");
        while (scanner.hasNext()){
            String next = scanner.next();
            System.out.println(next);
        }
    }
}
