/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.events;

/**
 * CustomApplicationEventListenerRegistry
 * 事件监听注册器 约定
 *
 * @author guoxing
 * @date 2020/11/23 4:52 PM
 * @since
 */
public interface CustomApplicationEventListenerRegistry {

    // 新增事件监听器
    void addEventListener(CustomApplicationEventListener<?> listener);

    // 删除事件监听器
    void removeEventListener(CustomApplicationEventListener<?> listener);

    // 获取所有注册的事件监听器
    CustomApplicationEventListener[] getCustomApplicationEventListeners();

    // 根据指定的事件类型获取事件监听器
    CustomApplicationEventListener[] getCustomApplicationEventListeners(Class<? extends CustomApplicationEvent> eventType);
}
