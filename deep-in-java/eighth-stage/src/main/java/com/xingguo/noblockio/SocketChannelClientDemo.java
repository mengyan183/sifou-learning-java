/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.noblockio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * SocketChannelClientDemo
 * {@link java.nio.channels.SocketChannel}
 *
 * @author guoxing
 * @date 2020/12/11 8:15 PM
 * @since
 */
public class SocketChannelClientDemo {

    public static void main(String[] args) throws IOException {
        /**
         * 对于client 端实际就不需要像{@link java.nio.channels.ServerSocketChannel}一样麻烦
         * 对于client 而言其就是一个普通的 {@link java.nio.channels.SocketChannel}
         */
        try (SocketChannel socketChannel = SocketChannel.open()) {
            // 设置当前未 no block
            socketChannel.configureBlocking(false);
            /**
             * 由于使用的 no block 模式,因此在 {@link SocketChannel#connect(SocketAddress)}时会直接返回false,并不会真正建立连接
             * 而是会等待{@link SocketChannel#finishConnect()}完成 nio 模式下的连接建立
             */
            // 指定要连接服务地址和端口
            boolean connect = socketChannel.connect(new InetSocketAddress(8080));
            // 完成 非阻塞式 连接建立
            while (!socketChannel.finishConnect()) {
                System.out.println("等待连接建立.....");
            }
            // 判断当前连接状态
            while (socketChannel.isConnected()) {
                // 创建一个 4k 的容器
                ByteBuffer byteBuffer = ByteBuffer.allocate(4 * 1024);
                // 将接受到的数据读取出来写入到byteBuffer中
                while (socketChannel.read(byteBuffer) != -1) {
                    // 准备读取 byteBuffer中的数据
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) {
                        System.out.print((char) byteBuffer.get());
                    }
                    byteBuffer.clear();
                }
                socketChannel.close();
            }
        }
    }
}
