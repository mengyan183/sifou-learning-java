/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.events;

import java.util.EventListener;

/**
 * CustomApplicationEventListener
 * 事件监听器 约束
 *
 * @author guoxing
 * @date 2020/11/23 4:45 PM
 * @since
 */
public interface CustomApplicationEventListener<E extends CustomApplicationEvent> extends EventListener {

    void onEvent(E event);
}
