/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.events;

import lombok.extern.slf4j.Slf4j;

/**
 * MyCustomApplicationEventListener
 * 事件监听器
 *
 * @author guoxing
 * @date 2020/11/23 5:15 PM
 * @since
 */
@Slf4j
public class MyCustomApplicationEventListener implements CustomApplicationEventListener<MyCustomApplicationEvent> {
    @Override
    public void onEvent(MyCustomApplicationEvent event) {
        // 处理事件
        log.info("{}处理{}事件", this.getClass(), event);
    }
}
