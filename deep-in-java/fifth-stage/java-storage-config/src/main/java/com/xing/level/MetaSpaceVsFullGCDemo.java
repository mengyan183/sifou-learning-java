/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xing.level;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * MetaSpaceVsFullGCDemo
 *
 * @author guoxing
 * @date 2020/10/23 4:01 PM
 * @since
 */
@Slf4j
public class MetaSpaceVsFullGCDemo {
    public static void main(String[] args) throws InterruptedException, IOException {
        int length = 100;
        String[] strings = new String[length];
//        test();
        int i = 0;
        //-Xmx10m -XX:MaxMetaspaceSize=10m -Xlog:gc+metaspace*
        while (true) {
            String intern = UUID.randomUUID().toString().intern();
            strings[i % length] = intern;
            if (++i % length == 0) {
                for (int j = 1; j < length; j++) {
                    strings[j] = intern.concat(strings[j - 1]).intern();
                }
//                System.gc();
                Thread.sleep(100);
            }
        }
    }

    private static void test() throws InterruptedException, IOException {
        Class<?> objectClass = Object.class;
        log.info("{}", System.identityHashCode(objectClass));
        String s = "S";
        log.info("{}", System.identityHashCode(s));
        s = null;
        objectClass = null;
        ArrayList<Object> objects = new ArrayList<>(1000);
//        for (int i = 0; i < 1000; i++) {
//            objects.add(new Object());
//        }
        System.gc();
        Thread.sleep(1000);
        s = "S";
        log.info("{}", System.identityHashCode(s));
        log.info("{}", System.identityHashCode(objectClass));
        System.in.read();
        int i = 0;
        while (true) {
            String intern = s.intern();
//            log.info("{}", System.identityHashCode(intern));
            objects.add(intern);
            if (i++ % 1000 == 0) {
                log.info("线程休眠");
                objects.clear();
                log.info("容器容量:{}", objects.size());
                System.gc();
                Thread.sleep(5000);
            }
        }
    }
}
