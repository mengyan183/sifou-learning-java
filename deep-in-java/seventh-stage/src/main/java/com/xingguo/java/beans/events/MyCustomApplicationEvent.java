/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.events;

/**
 * MyCustomApplicationEvent
 * 自定义事件
 *
 * @author guoxing
 * @date 2020/11/23 5:16 PM
 * @since
 */
public class MyCustomApplicationEvent extends CustomApplicationEvent {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MyCustomApplicationEvent(Object source) {
        super(source);
    }
}
