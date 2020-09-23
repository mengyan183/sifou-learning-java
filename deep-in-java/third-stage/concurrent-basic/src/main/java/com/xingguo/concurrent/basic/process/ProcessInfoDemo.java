/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.basic.process;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

/**
 * ProcessInfoDemo
 * 进程信息
 *
 * @author guoxing
 * @date 2020/9/23 5:49 PM
 * @since
 */
public class ProcessInfoDemo {
    public static void main(String[] args) {
        printProcessIdBeforeJava9();
        printProcessIdAfterJava9();

        printProcessInfo();
        // 进程退出
        System.exit(0);
    }

    private static void printProcessInfo() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        System.out.println("进程启动时间:" + LocalDateTime.ofInstant(Instant.ofEpochMilli(runtimeMXBean.getStartTime()), ZoneId.systemDefault()));
        System.out.println("进程已运行时间:" + runtimeMXBean.getUptime());

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        System.out.println("当前进程中的存活的线程数量:" + threadMXBean.getThreadCount());

        ManagementFactory.getMemoryManagerMXBeans().forEach(memoryManagerMXBean -> {
            System.out.println("内存相关信息:当前内存区域名称:" + memoryManagerMXBean.getName());
            Stream.of(memoryManagerMXBean.getMemoryPoolNames()).forEach(System.out::println);
        });
    }

    public static void printProcessIdAfterJava9() {
        System.out.println("当前进程id为:" + ManagementFactory.getRuntimeMXBean().getPid());
        System.out.println("当前进程id为:" + ProcessHandle.current().pid());
    }

    public static void printProcessIdBeforeJava9() {
        // 获取运行时系统消息
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String name = runtimeMXBean.getName();
        // 格式为 :15114@xingguodeMacBook-Pro.local
        System.out.println(name);
        System.out.println("当前进程id为:" + name.substring(0, name.indexOf("@")));
    }
}
