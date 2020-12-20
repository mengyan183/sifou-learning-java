/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.spi.driver;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * SqlDriverDemo
 * //TODO : 要求项目中需要自定义引入 mysql-connector-java-版本号.jar
 * @author guoxing
 * @date 2020/12/20 1:04 PM
 * @since
 */
public class SqlDriverDemo {

    public static void main(String[] args) {
        ServiceLoader<Driver> load = ServiceLoader.load(Driver.class);
        Iterator<Driver> iterator = load.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
