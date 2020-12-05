/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.operate;

import java.io.DataOutputStream;

/**
 * DataStreamDemo
 * {@link java.io.DataOutputStream}
 *
 * @author guoxing
 * @date 2020/12/5 7:09 PM
 * @since
 */
public class DataStreamDemo {
    public static void main(String[] args) throws Exception {
        DataOutputStream dataOutputStream = new DataOutputStream(System.out);
        // 使用 int数值类型进行写入数据时,实际上输入的数据为 ascii 码
        dataOutputStream.writeByte(97);
        dataOutputStream.writeByte('a');
        int a = (int) 'A';
        System.out.println(97-a);
        dataOutputStream.writeChar(a);
        dataOutputStream.writeChar('A');

        dataOutputStream.writeUTF("hello world");
    }
}
