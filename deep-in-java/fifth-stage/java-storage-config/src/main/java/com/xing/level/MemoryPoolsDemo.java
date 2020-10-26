/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xing.level;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;

/**
 * MemoryPoolsDemo
 * 内存池
 *
 * @author guoxing
 * @date 2020/10/22 10:42 AM
 * @since
 */
@Slf4j
public class MemoryPoolsDemo {
    public static void main(String[] args) throws IOException {
        ManagementFactory.getMemoryManagerMXBeans().forEach(
                memoryManagerMXBean -> {
                    // 对于 memoryManagerMXBean的名称实际就是对应其垃圾回收算法
                    // 对于 后续的 getMemoryPoolNames 表示当前算法操作的内存区域
                    log.info("{} 内存区域 管理的jvm内存池{}", memoryManagerMXBean.getName(), Arrays.asList(memoryManagerMXBean.getMemoryPoolNames()));
                }
        );
    }
}
