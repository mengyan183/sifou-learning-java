/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.instrument;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * VirtualMachineDemo
 * {@link VirtualMachine}
 *
 * @author guoxing
 * @date 2020/11/30 9:09 PM
 * @since
 */
@Slf4j
public class VirtualMachineDemo {
    public static void main(String[] args) {
        String name = VirtualMachineDemo.class.getName();
        log.info("{}", name);
        // 当前操作会返回当前jvm全部运行的 进程信息 类似于 jps命令操作
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        list.forEach(virtualMachineDescriptor ->
        {
            log.info("{}", virtualMachineDescriptor);
            if (name.equals(virtualMachineDescriptor.displayName())) {
                try {
                    // attach 当前 进程
                    VirtualMachine attach = VirtualMachine.attach(virtualMachineDescriptor.id());
//                    attach.loadAgent();
                    log.info("{}", attach);
                } catch (AttachNotSupportedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
