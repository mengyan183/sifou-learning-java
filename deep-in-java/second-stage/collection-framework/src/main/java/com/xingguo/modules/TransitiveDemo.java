/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.modules;

import java.util.logging.Logger;

/**
 * TransitiveDemo
 * 模块依赖传递
 *
 * @author guoxing
 * @date 9/14/20 8:58 PM
 * @since
 */
public class TransitiveDemo {
    /**
     * 可以看到在当前的启动命令中 存在 文件目录 以及 具体的jar包 ,这里代表的就是模块路径
     *
     * -p /Users/xingguo/sifou-learning-java/deep-in-java/second-stage/collection-framework/target/classes:
     * /Users/xingguo/.m2/repository/org/springframework/spring-context/5.2.8.RELEASE/spring-context-5.2.8.RELEASE.jar:
     * /Users/xingguo/.m2/repository/org/springframework/spring-aop/5.2.8.RELEASE/spring-aop-5.2.8.RELEASE.jar:
     * /Users/xingguo/.m2/repository/org/springframework/spring-beans/5.2.8.RELEASE/spring-beans-5.2.8.RELEASE.jar:
     * /Users/xingguo/.m2/repository/org/springframework/spring-core/5.2.8.RELEASE/spring-core-5.2.8.RELEASE.jar:
     * /Users/xingguo/.m2/repository/org/springframework/spring-jcl/5.2.8.RELEASE/spring-jcl-5.2.8.RELEASE.jar:
     * /Users/xingguo/.m2/repository/org/springframework/spring-expression/5.2.8.RELEASE/spring-expression-5.2.8.RELEASE.jar
     * @param args
     */
    public static void main(String[] args) {
        Logger logger;// 由于依赖了java.sql中存在 transitive java.logging,因此可以直接使用java.logging中的代码, 而当不直接或间接依赖 java.logging时,当前代码会编译错误
        logger = Logger.getLogger("TransitiveDemo");
        logger.info("1");
    }
}
