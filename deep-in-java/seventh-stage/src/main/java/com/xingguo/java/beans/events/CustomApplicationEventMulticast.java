/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.events;

import lombok.extern.slf4j.Slf4j;

/**
 * CustomApplicationEventMulticast
 * 事件广播
 *
 * @author guoxing
 * @date 2020/11/23 4:58 PM
 * @since
 */
@Slf4j
public class CustomApplicationEventMulticast {

    private final CustomApplicationEventListenerRegistry registry;

    public CustomApplicationEventMulticast() {
        this.registry = new SimpleApplicationEventListenerRegistry();
    }

    public CustomApplicationEventMulticast(CustomApplicationEventListenerRegistry registry) {
        this.registry = registry;
    }

    public void addListener(CustomApplicationEventListener customApplicationEventListener) {
        registry.addEventListener(customApplicationEventListener);
    }


    public CustomApplicationEventListener[] getCustomApplicationEventListeners() {
        return registry.getCustomApplicationEventListeners();
    }

    public CustomApplicationEventListener[] getCustomApplicationEventListeners(Class<? extends CustomApplicationEvent> eventType) {
        return registry.getCustomApplicationEventListeners(eventType);
    }

    public void multicast(CustomApplicationEvent event) {
        // 获取当前注册器中已存在的当前事件类型的listener
        CustomApplicationEventListener[] customApplicationEventListeners = getCustomApplicationEventListeners(event.getClass());
        for (CustomApplicationEventListener customApplicationEventListener : customApplicationEventListeners) {
            // 处理当前事件
            customApplicationEventListener.onEvent(event);
        }
    }

    public static void main(String[] args) {
        CustomApplicationEventMulticast customApplicationEventMulticast = new CustomApplicationEventMulticast();
        for (int i = 0; i < 2; i++) {
            customApplicationEventMulticast.addListener(new MyCustomApplicationEventListener());
        }

        customApplicationEventMulticast.multicast(new MyCustomApplicationEvent("hello world"));
    }
}
