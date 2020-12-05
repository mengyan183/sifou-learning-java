/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.bytestream;

import java.io.*;

/**
 * BufferedInputStreamDemo
 *
 * @author guoxing
 * @date 2020/12/3 3:24 PM
 * @since
 */
public class BufferedInputStreamDemo {

    /**
     * 对于bufferedInputStream为了满足不同输入输出速度的IO设备进行交互,可以通过buffer 进行临时数据缓存(预读取),可以通过当前操作达到预读取的目的,提高数据传输效率
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        try (
                InputStream systemStream = System.in;
                BufferedInputStream bufferedInputStream = new BufferedInputStream(systemStream);
                OutputStream systemStreamOut = System.out;
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(systemStreamOut);
        ) {
            byte[] bytes = new byte[8192];
            while (bufferedInputStream.read(bytes) != -1) {
                bufferedOutputStream.write(bytes);
            }
        }
    }
}
