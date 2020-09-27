/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicDemo
 * 原子操作
 *
 * @author guoxing
 * @date 2020/9/27 3:59 PM
 * @since
 */
public class AtomicDemo {

    public static void main(String[] args) {
        atomicIntegerDemo();

    }

    public static void atomicReferenceDemo(){
        AtomicReference<Object> objectAtomicReference = new AtomicReference<>();
        // 对于atomicReference 实际只能保证当前对象的原子操作,但其并不存在传递性,并不能保证对象中的属性操作原子性

    }

    public static void atomicLongDemo() {
        AtomicLong atomicLong = new AtomicLong(0L);
        // 对于atomicLong 的set 也是利用的cas操作,并非直接使用volatile
        //原因在于 long(64) 在32位系统中存储时分为高位和低位,因此在写入操作时 实际是两步操作,并不能单纯使用 volatile 来保证
        atomicLong.set(1L);
        atomicLong.get();
    }

    public static void atomicIntegerDemo() {
        // 对于 低于32位的数据统一都使用32位int进行存储
        // int 表示 int(32), char(16), short(16), byte(8), boolean(1)
        AtomicInteger atomicInteger = new AtomicInteger(0);
        // 对于 get和set 实际是利用了 volatile (内存屏障)的语义
        atomicInteger.set(1);
        int i = atomicInteger.get();
        // 对于cas相关操作是利用的 unsafe中的cas操作
        i = atomicInteger.compareAndExchange(1, 2);
        assert i == 2;
    }

    public static void atomicBooleanDemo() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        // 对于atomicBoolean底层实际也是利用的int存储的数据 : 0 -> false ; 1 -> true
    }


    private static void getVarHandle() {
        MethodHandles.Lookup l = MethodHandles.lookup();
    }

    private static void getUnsafe() throws PrivilegedActionException {
        // 无法通过正常方法调用
//        Unsafe unsafe = Unsafe.getUnsafe();

        // https://github.com/mercyblitz/confucius-commons/blob/master/confucius-commons-lang/src/main/java/org/confucius/commons/lang/misc/UnsafeUtils.java
        final PrivilegedExceptionAction<Unsafe> action = () -> {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        };

        Unsafe unsafe = AccessController.doPrivileged(action);

        if (unsafe == null) {
            throw new NullPointerException();
        }

    }
}
