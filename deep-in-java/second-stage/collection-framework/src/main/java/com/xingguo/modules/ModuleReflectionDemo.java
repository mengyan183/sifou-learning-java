/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.modules;

import java.lang.module.ModuleDescriptor;
import java.sql.Driver;
import java.util.logging.Logger;

/**
 * ModuleReflectionDemo
 * 模块反射
 *
 * @author guoxing
 * @date 9/15/20 1:18 PM
 * @since
 */
public class ModuleReflectionDemo {
    public static void main(String[] args) {
        Class<ModuleReflectionDemo> moduleReflectionDemoClass = ModuleReflectionDemo.class;
        // 获取当前类所在module
        Module module = moduleReflectionDemoClass.getModule();
        System.out.println("当前模块名称为:" + module.getName());
        ModuleDescriptor descriptor = module.getDescriptor();
        // 当前模块中全部的依赖使用
        descriptor.requires().forEach(requires -> {
            System.out.println("当前模块全部的依赖:" + requires.name() + "当前引入依赖的传递性定义:" + requires.modifiers());
        });
        descriptor.exports().forEach(exports -> {
            System.out.println("当前模块暴露的包名为:" + exports.source() + ",指定导出到的依赖为:" + exports.targets());
        });
        descriptor.uses().forEach(serviceName -> {
            System.out.println("当前模块暴露的服务(SPI)" + serviceName);
        });
        descriptor.provides().forEach(impl -> {
            System.out.println("当前模块对暴露服务的具体实现类" + impl);
        });
        // 打印Driver所在sql模块的依赖信息
        Driver.class.getModule().getDescriptor().requires().forEach(
                requires -> {
                    System.out.println("当前模块全部的依赖:" + requires.name() + "当前引入依赖的传递性定义:" + requires.modifiers());
                }
        );

    }
}
