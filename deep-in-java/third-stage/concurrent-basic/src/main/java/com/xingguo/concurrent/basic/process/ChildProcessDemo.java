/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.basic.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * ChildProcessDemo
 * 子进程
 *
 * @author guoxing
 * @date 2020/9/23 8:24 PM
 * @since
 */
public class ChildProcessDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        String name = operatingSystemMXBean.getName();
        System.out.println(name);
        if (name.startsWith("Mac")) {
            // 启动一个子进程,打开计算器
            Process exec = Runtime.getRuntime().exec("open -a Calculator");
            while (exec.isAlive()) {
                Thread.sleep(3000);
                exec.destroy();
                System.out.println(exec.isAlive());
            }
        }
    }
}
