/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.noblockio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * SelectorClientDemo
 *
 * @author guoxing
 * @date 2020/12/12 10:38 AM
 * @since
 */
public class SelectorClientDemo {
    public static void main(String[] args) {
        // 创建客户端
        try (SocketChannel socketChannel = SocketChannel.open()) {
            // 设置为非阻塞
            socketChannel.configureBlocking(false);
            // 和服务端建立连接
            socketChannel.connect(new InetSocketAddress(8080));
            if (!socketChannel.finishConnect()) {
                System.out.println("等待和服务端连接建立完成.......");
            }
            ByteBuffer allocate = ByteBuffer.allocate(8);
            while (socketChannel.read(allocate) != -1) {
                allocate.flip();
                while (allocate.hasRemaining()) {
                    long aLong = allocate.getLong();
                    System.out.println(new Date(aLong));
                }
                allocate.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
