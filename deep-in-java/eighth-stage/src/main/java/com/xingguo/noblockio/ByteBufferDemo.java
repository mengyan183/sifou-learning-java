/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.noblockio;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * ByteBufferDemo
 * {@link java.nio.ByteBuffer}
 *
 * @author guoxing
 * @date 2020/12/10 9:30 AM
 * @since
 */
public class ByteBufferDemo {
    public static void main(String[] args) {
//        createBuffer();

        readAndWrite();

    }

    private static void readAndWrite() {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        printInfo(allocate);
        System.out.println("start write");
        byte[] bytes = new byte[]{(byte) 1, (byte) 2, (byte) 3};
        /**
         * 当写入数据时,{@link Buffer#position}表示的是待写入位置, 且{@link ByteBuffer#put(byte)} 会进行 {@link ByteBuffer#position}和{@link ByteBuffer#limit}判断,要求写入操作中 position < limit否则会抛出异常
         * 对于写入操作 {@link Buffer#limit}数据是不会变化的
         */
        for (byte b : bytes) {
            allocate.put(b);
            printInfo(allocate);
        }
        System.out.println("end write");
        System.out.println("when finish write,if next operate is read, we can operate limit and position help read");
        allocate.flip();
        // 由于 flip 操作中 limit = position; position = 0;mark = -1  因此就不需要 rewind 操作
//        System.out.println("before read start, rewind position ,move position to zero");
//        allocate.rewind();
        printInfo(allocate);
        System.out.println("start read");
        /**
         * 对于读取{@link ByteBuffer#get()}操作,实际就是 通过移动 {@link ByteBuffer#position} 来读取数据,直到{@link ByteBuffer#position}移动到 {@link ByteBuffer#limit}位置
         * 因此当执行读取操作时我们需要先将 {@link ByteBuffer#position} 归零 {@link ByteBuffer#rewind()}
         * 由于在{@link ByteBuffer#flip()}中将 写入操作结束后的游标 {@link ByteBuffer#position} 赋值给 {@link ByteBuffer#limit} ,当前操作实际是对后续读取操作的优化, 避免会读取到上次写入操作未操作的空间,对于当前优化操作针对{@link java.nio.DirectByteBuffer}优化更明显
         * 对于 {@link ByteBuffer#clear()}和{@link ByteBuffer#flip()} 操作的不同点在于  {@link ByteBuffer#limit}的操作,对于clear中是将 {@link ByteBuffer#capacity}赋值给{@link ByteBuffer#limit},相当于可用空间为整个字节数组
         */
        byte b = allocate.get(0); // 对于当前指定索引位置读取,实际也会进行limit判断限制,但不会移动position
        while (allocate.hasRemaining()) { // 当前判断实际 就是 判断 position 是否 满足 limit 的限制
            allocate.get();
            printInfo(allocate);
        }
        System.out.println("end read");
        /**
         * 对于{@link ByteBuffer#compact()}操作实际就是将{@link ByteBuffer#position}位置开始到{@link ByteBuffer#limit} 位置的全部元素从 0位置开始写入,对于新的{@link ByteBuffer#position}位置后的数据实际就是无效数据了
         */
        allocate.compact();

    }

    private static void createBuffer() {
        // 创建一个 HeapByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(16); // mark:-1 , position: 0 , limit : 16, capacity: 16
        printInfo(byteBuffer);
        // 通过外部传递一个 byte数组 创建 byteBuffer
        byte[] bytes = new byte[10];
        /**
         * 当传递初始偏移量 {@link HeapByteBuffer#offset} 会限制其读取开始位置 {@link HeapByteBuffer#get(int)},当参数传递 length 时实际是对{@link java.nio.HeapByteBuffer#limit}进行赋值,其值为 offset+length
         * 对于以下代码 offset:5,length:4  对于ByteBuffer实际是读取的 {@code bytes}中 [5,9)区间的数据
         */
        ByteBuffer wrap = ByteBuffer.wrap(bytes, 5, 4); // mark:-1,position:5,limit:9,capacity:10, offset:0

        printInfo(wrap);
    }


    private static void printInfo(ByteBuffer byteBuffer) {
        System.out.printf("mark:IS PRIVATE,position:%d,limit:%d,capacity:%d\n",
                byteBuffer.position(),
                byteBuffer.limit(),
                byteBuffer.capacity());
    }
}
