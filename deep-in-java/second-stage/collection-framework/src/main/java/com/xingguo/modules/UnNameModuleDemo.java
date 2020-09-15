/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.modules;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;

/**
 * UnNameModuleDemo
 * 未命名模块依赖 : 例如通过maven引入的spring
 *
 * @author guoxing
 * @date 9/15/20 11:11 AM
 * @since
 */
public class UnNameModuleDemo {
    public static void main(String[] args) {
        // 虽然使用maven引入了当前spring-context依赖, 但由于模块化的原因,引入的jar并不能直接使用,而需要引入依赖
        // 在 spring context目录下中存在 Automatic-Module-Name: spring.context
        // 因此当需要使用第三方未命名模块jar时,需要通过其指定的module-name进行引入才能直接使用未命名模块jar
        ConfigurableApplicationContext configurableApplicationContext = new AnnotationConfigApplicationContext();
        configurableApplicationContext.close();

        StringUtils.isNotBlank("1");
        CollectionUtils.isEmpty(new ArrayList<String>());
    }
}
