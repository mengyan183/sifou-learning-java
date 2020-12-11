/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.nio;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * ByteChannelDemo
 * {@link java.nio.channels.ByteChannel}
 * 作为 new io 中新提供的操作,实际使用了{@link java.nio.ByteBuffer} 对于 其 concrete class 包含{@link java.nio.HeapByteBuffer} 以及 {@link java.nio.DirectByteBuffer}, 其和 {@link java.io.BufferedInputStream} 的区别在于 对于{@link java.io.BufferedInputStream}由于其使用了{@code byte[]} 因此其只支持 堆内数据 等价于 {@link java.nio.HeapByteBuffer}
 * 对于 {@link java.nio.DirectByteBuffer} 实际就是也是存储到内存中的,只是并不直接存储到堆空间中
 *
 * @author guoxing
 * @date 2020/12/8 9:18 PM
 * @since
 */
public class ByteChannelDemo {
    private static final String USER_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {

        createAndWrite();

//        byteBufferReadData();
    }

    /**
     * 创建新的文件和数据写入
     */
    private static void createAndWrite() {
        try (
                ByteChannel source = Files.newByteChannel(Paths.get(USER_DIR, "README.md"));
                // 对于创建文件和写入数据需要增加 操作参数  每次都创建新的文件以及数据写入 , 关闭时删除
                ByteChannel target = Files.newByteChannel(Paths.get(USER_DIR, "COPY-README.md"), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE, StandardOpenOption.DELETE_ON_CLOSE);
        ) {
            // 对于当前容量可以尽可能大一些,减少文件读取次数
            ByteBuffer byteBuffer = ByteBuffer.allocate(64);
            while (source.read(byteBuffer) > 0) {
                byteBuffer.rewind();
                //TODO 当前操作也会存在乱码的问题
                target.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void byteBufferReadData() {
        /**
         * 测试 使用 {@link Files#newByteChannel(Path, OpenOption...)} 来进行文件读取和写入操作
         * 对于{@link OpenOption}的 concrete class {@link java.nio.file.StandardOpenOption} 中的操作包含了其他语言都通用的文件io操作
         */
        // 读取 USER_DIR/README.md 文件

        try (ByteChannel byteChannel = Files.newByteChannel(Paths.get(USER_DIR, "README.md"))) {
            // 创建一个{@link HeapByteBuffer}
            /**
             * TODO : 对于当前数据读取中文存在乱码的情况; 由于中文字符UTF-8编码集字节长度不固定,因此会导致按照字节读取时出现一个完整的字符字节被中断的情况
             * 对于 当前初始化的 {@link ByteBuffer}中的相关初始化字段
             * {@link ByteBuffer#mark} = -1
             * {@link ByteBuffer#position} = 0
             * {@link ByteBuffer#capacity} = {@link ByteBuffer#allocate(int)} 中的参数
             * {@link ByteBuffer#limit} = {@link ByteBuffer#allocate(int)} 中的参数
             */
            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
            Charset charset = StandardCharsets.UTF_8;
            // 对于 read 的终止条件为 0或-1 代表文件读取结束
            /**
             * 对于 {@link ByteBuffer#put(byte)}操作实际会变更{@link ByteBuffer#position} 记录写入 bytebuffer的字节长度
             */
            while (byteChannel.read(byteBuffer) > 0) {
                /**
                 * rewind 倒带的含义表示 对于bytebuffer每次数据读取都是从头{@link ByteBuffer#position} = 0 开始进行数据读取;由于当写入完成后,{@link ByteBuffer#position}位置发生变化,因此数据读取时要从头开始读取
                 */
                byteBuffer.rewind();
                /**
                 * 对于{@link Charset#decode(ByteBuffer)}操作实际就是读取 {@link ByteBuffer},会调用{@link ByteBuffer#position(int)}修改{@link ByteBuffer#position}字段数据,记录本次读取bytebuffer的字节长度
                 */
                // 使用UTF-8编码集 将 byteBuffer 转换为 CharBuffer
                CharBuffer charBuffer = charset.decode(byteBuffer);
                // 对于当前 操作 默认是按照 bigEndian字节读取方式进行读取数据,因此读取出来的数据会是乱码
//                CharBuffer charBuffer = byteBuffer.asCharBuffer();
                /**
                 * 由于 {@link CharBuffer} {@code implements CharSequence}
                 * 对于 {@link CharBuffer#toString(int, int)} 中的 start 字段 是从 position 开始到 limit 结束
                 */
                System.out.print(charBuffer);
                /**
                 * 由于读取操作导致 {@link ByteBuffer#position} 数据发生变化,
                 * 对于{@link ByteBuffer#limit} 字段实际是通过记录上一次的数据读取的长度,limit 的另一个作用是避免读取到错误的数据,限制读取长度; limit 限制的是实际可用容量
                 *
                 * flip 翻转
                 * {@link ByteBuffer#flip()} 首先将上一次读取的数据长度{@link ByteBuffer#position}赋值给{@link ByteBuffer#limit},并将 {@link ByteBuffer#position}重置为0 ,以及{@link ByteBuffer#mark}重置为-1
                 *
                 * 对于{@link Buffer#flip()}和{@link Buffer#clear()} 的唯一区别在于 对于{@link Buffer#limit}字段的操作
                 * 对于 {@link Buffer#flip()} 操作的好处在于记录了上次读取的最长长度,同时也限制了下一次写入的长度
                 */
//                byteBuffer.flip();
                // 由于下一次是写入操作,因此不需要 flip操作,对于下一次写入支持将byte数组的数据全部覆盖
                byteBuffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
