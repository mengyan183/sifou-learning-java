/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.events;

import java.util.EventObject;

/**
 * CustomApplicationEvent
 * 参考 ApplicationEvent
 *
 * @author guoxing
 * @date 2020/11/23 4:41 PM
 * @see java.util.EventObject
 * @see org.springframework.context.ApplicationEvent
 * @since
 */
public class CustomApplicationEvent extends EventObject {

    // 时间戳
    private final long timeStamp;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public CustomApplicationEvent(Object source) {
        super(source);
        this.timeStamp = System.currentTimeMillis();
    }
}
