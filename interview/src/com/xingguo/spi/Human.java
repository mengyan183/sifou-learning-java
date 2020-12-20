/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.spi;

/**
 * Human
 *
 * @author guoxing
 * @date 2020/12/20 12:19 PM
 * @since
 */
public class Human implements Animal {
    @Override
    public void eat() {
        System.out.println("human eat anything what they can eat");
    }
}
