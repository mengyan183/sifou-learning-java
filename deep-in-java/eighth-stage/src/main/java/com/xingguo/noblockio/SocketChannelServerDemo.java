/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.noblockio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * SocketChannelServerDemo
 * {@link java.nio.channels.ServerSocketChannel}
 * 测试socket 套接字技术
 *
 * @author guoxing
 * @date 2020/12/11 7:51 PM
 * @since
 */
public class SocketChannelServerDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        /**
         * 利用{@link java.nio.channels.ServerSocketChannel} 创建一个server
         */
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // 绑定一个端口,表示当前服务暴露端口
            serverSocketChannel.bind(new InetSocketAddress(8080));
            /**
             * {@link java.nio.channels.SocketChannel}支持 no block io
             * 支持手动设置开启
             */
            serverSocketChannel.configureBlocking(false);
            // 获取当前地址
            SocketAddress localAddress = serverSocketChannel.getLocalAddress();
            System.out.printf("当前服务地址为:%s\n", localAddress);
            // 设置当前要发送的内容
            String message = "hello,world";

            while (true) {
                // 在no block 连接模式下,  {@link ServerSocketChannel#accept} {@code return null} ,等待client建立连接
                // 因此可以通过当前方法判断是否存在连接
                // 获取当前成功建立的连接
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (Objects.nonNull(socketChannel)) {
                    System.out.printf("当前连接的客户端地址为:%s;\n", socketChannel.getRemoteAddress());
                    // 将服务端要发送的数据转换为 bytebuffer
                    ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
                    // 发送数据
                    socketChannel.write(byteBuffer);
                    // 关闭当前连接
                    socketChannel.close();
                    // 终止循环
                    break;
                } else {
                    // 当没有客户端连接时,线程休眠
                    Thread.sleep(1000L);
                }
            }
        }
    }
}
