/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.noblockio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

/**
 * ChannelDemo
 * 对比 io中的 {@link java.io.InputStream,java.io.OutputStream} 和 new io 中的 {@link java.nio.channels.Channel,java.nio.channels.ReadableByteChannel,java.nio.channels.WritableByteChannel}
 *
 * 模拟 commons-io 中的{@link org.apache.commons.io.IOUtils#copy(InputStream, OutputStream)}
 *
 * @author guoxing
 * @date 2020/12/11 3:57 PM
 * @since
 */
public class ChannelDemo {
    private static final String lineSeparator = System.getProperty("line.separator");

    public static void main(String[] args) throws IOException {
//        elderIO();
        channelCopy();
    }

    /**
     * 使用 {@link java.nio.channels.Channel} 实现 数据传输
     * {@link Channels}
     * 数据读取使用 {@link java.nio.channels.ReadableByteChannel}
     * 数据写入使用 {@link java.nio.channels.WritableByteChannel}
     * 使用 {@link ByteBuffer} 进行数据中转
     *
     * @author guoxing
     * @date 2020-12-11 4:29 PM
     * @since
     */
    private static void channelCopy() throws IOException {
        ReadableByteChannel readableByteChannel = Channels.newChannel(System.in);
        WritableByteChannel writableByteChannel = Channels.newChannel(System.out);
        ByteBuffer allocate = ByteBuffer.allocate(4 * 1024);
        while (readableByteChannel.isOpen() && readableByteChannel.read(allocate) > 0) {
            if (new String(allocate.array(), 0, allocate.position(), StandardCharsets.UTF_8).startsWith("exit" + lineSeparator)) {
                readableByteChannel.close();
                writableByteChannel.close();
                break;
            }
            allocate.rewind();
            if (allocate.hasRemaining()) {
                writableByteChannel.write(allocate);
            }
            allocate.clear();
        }

    }

    /**
     * 使用 普通 io 实现  input / output 操作
     * 当输入 exit 回车时,就会退出
     *
     * @author guoxing
     * @date 2020-12-11 4:03 PM
     * @since
     */
    private static void elderIO() throws IOException {
        /**
         * 其文件描述符为 {@link java.io.FileDescriptor} {@link FileDescriptor#in}
         */
        InputStream in = System.in;
        /**
         * 其文件描述符为 {@link FileDescriptor} {@link FileDescriptor#out}
         */
        PrintStream out = System.out;
        // 使用byte 数组作为数据传输介质
        byte[] bytes = new byte[4 * 1024];
/*        // 将inputStream中的数据写入到字节数组中
        while (in.read(bytes) != -1) {
            String s = new String(bytes, StandardCharsets.UTF_8);
            if (s.startsWith("exit" + lineSeparator)) { // 当前代码在前端编译时会被优化,直接变成常量存储
                in.close();
                out.close();
                break;
            }
            out.write(bytes);
            // 保险起见,清空字节数组
            bytes = new byte[8];
        }*/

        // 优化方案 : 通过记录每次操作的字节长度,限定可用空间
        // 读取到的字节长度
        int length;
        while ((length = in.read(bytes)) != -1) {
            String s = new String(bytes, 0, length, StandardCharsets.UTF_8);
            if (s.startsWith("exit" + lineSeparator)) { // 当前代码在前端编译时会被优化,直接变成常量存储
                in.close();
                out.close();
                break;
            }
            out.write(bytes, 0, length);
        }

    }

}
