/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.spi;

/**
 * Cat
 *
 * @author guoxing
 * @date 2020/12/20 12:18 PM
 * @since
 */
public class Cat implements Animal {
    @Override
    public void eat() {
        System.out.println("cat eat fish!!!");
    }
}
