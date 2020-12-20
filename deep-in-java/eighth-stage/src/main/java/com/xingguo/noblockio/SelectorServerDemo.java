/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.noblockio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * SelectorServerDemo
 * 对于 {@link java.nio.channels.Selector}
 * 由于不同的操作系统实际会存在不同的实现方式 select 和 epoll
 * <p>
 * 对于 no block io 的标准模板是 连接复用, 当只使用 {@link ServerSocketChannel#accept()}实际是每次都会建立新的连接;
 * 而 {@link java.nio.channels.Selector} 通过注册多个channel 与socketChannel 进行关联,通过管理channel的方式,来实现Socket的复用,从而体现了"多路复用"
 *
 * @author guoxing
 * @date 2020/12/11 4:49 PM
 * @since
 */
public class SelectorServerDemo {

    public static void main(String[] args) throws IOException {
        fixSelectorServer();
//        correctSelector();

    }

    private static void correctSelector() throws IOException {
        // 创建服务端 serverSocketChannel
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // 将当前serverSocketChannel绑定 8080 端口
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));
            // 设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 创建 Selector
            Selector selector = Selector.open();
            // 注册  selector 和 serverSocket
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // 获取当前 select 已成功建立的连接(channel)数量
                int select = selector.select();
                if (select == 0) {
                    // 继续等待连接建立
                    continue;
                }
                // 获取当前全部成功建立连接的 selectionKey
                // 对于 selectionKey 实际 就是 selectableChannel 和 socket 连接建立的体现
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    // 判断当前selector连接状态
                    if (next.isAcceptable()) {
                        // 对于 selectionKey 中存储了 serverSocketChannel相关数据信息
                        ServerSocketChannel socketChannel = (ServerSocketChannel) next.channel();
                        // 对于 selector 和 socketChannel的区别就在于 ,selector作为 server 和 client的中间媒介,而socketChannel交由selectionKey进行管理
                        SocketChannel accept = socketChannel.accept();
                        // 由于no block mode 可能连接并不会立即建立成功
                        if (accept == null) {
                            System.out.println("等待客户端连接当前服务");
                            continue;
                        }
                        // 准备server 要发送的数据
                        ByteBuffer allocate = ByteBuffer.allocate(8);
                        // 传递当前时间
                        allocate.putLong(System.currentTimeMillis());
                        allocate.flip();
                        // channel准备发送数据
                        accept.write(allocate);
                        // 由于 allocate 是临时变量,因此就不需要在进行复位(clear)操作
                        accept.close();
                        System.out.println("服务端发送数据成功");
                    }

                    iterator.remove();
                }
            }
        }
    }

    private static void fixSelectorServer() {
        // 创建一个 serverSocketChannel
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // 绑定一个端口 8080
            serverSocketChannel.bind(new InetSocketAddress(8080));
            // 设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            System.out.println("当前服务器地址：" + serverSocketChannel.socket().getLocalSocketAddress());
            // 创建一个 selector, 用于管理 注册的channel 和 读取状态
            try (Selector selector = Selector.open()) {
                // 绑定 selector 和 socket 的关系
                // 对于 OP_ACCEPT 实际包含了所有的类型
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                // 对于当前返回的数据only read
//                selector.keys();

                while (true) {
                    System.out.println("selector is open:" + selector.isOpen());
                    // 获取已成功建立连接的 SelectableChannel 数量
                    int select = selector.select();
                    System.out.println("select is " + select);
                    if (select == 0) {
                        // 继续等待连接建立
                        continue;
                    }
                    // 返回所有成功建立连接的 selectionKey
                    // 其表示的是 SelectableChannel 和 SelectionKey 的关系
                    // 对于当前返回的数据是 不可新增操作集合
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isAcceptable()) {
                            // selectionKey封装了channel 和 selector的关系
                            ServerSocketChannel socketChannel = (ServerSocketChannel) selectionKey.channel();

                            // 判断当前的连接是否完成建立
                            SocketChannel accept = socketChannel.accept();
                            if (accept == null) {
                                continue;
                            }
                            System.out.printf("接收到客户端请求:[%s]\n", accept.getRemoteAddress());
                            ByteBuffer allocate = ByteBuffer.allocate(8);
                            // 写入当前时间
                            allocate.putLong(System.currentTimeMillis());
                            // 游标复位
                            allocate.flip();
                            while (allocate.hasRemaining()) {
                                // 将数据数据发送给客户端
                                accept.write(allocate);
                            }
                            // 关闭当前连接
                            // socketChannel.close(); // this is a bug; 当关闭了socketChannel,但由于selectableChannel没有关闭,实际对于 client 而言,相当于连接一直存在
                            /**
                             * selector 提供的功能实际 就是 server 复用,
                             * 对于 原始的 serverSocket 和 socket(client) , 连接关系实际为 server <-> client
                             * 当 使用{@link ServerSocketChannel#register(Selector, int)} 时, 连接关系就变成了  servers <-> sequence of {@link java.nio.channels.SelectableChannel} ; {@link java.nio.channels.SelectableChannel} <-> client ; 连接关系变为了 n(server) : n(selectableChannel) : n(client)
                             */
                            //TODO warning 关闭正确的连接
                            accept.close();
                            System.out.println("服务端数据发送完成");
                        }
                        // 移除已接收的key
                        iterator.remove();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
