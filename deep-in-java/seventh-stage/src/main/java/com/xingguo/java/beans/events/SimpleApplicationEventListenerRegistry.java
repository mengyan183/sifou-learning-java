/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.events;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SimpleApplicationEventListenerRegistry
 * 自定义事件监听器
 *
 * @author guoxing
 * @date 2020/11/23 5:01 PM
 * @since
 */
public class SimpleApplicationEventListenerRegistry implements CustomApplicationEventListenerRegistry {

    // 设置使用自定义比较器的treeset
    private final Set<CustomApplicationEventListener> listeners = new TreeSet<>(new CustomListenerComparator());

    // 根据 listener 的泛型参数类型来存储
    private final Map<String, Set<CustomApplicationEventListener>> listenerMap = new HashMap<>();

    @Override
    public void addEventListener(CustomApplicationEventListener<?> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeEventListener(CustomApplicationEventListener<?> listener) {
        listeners.remove(listener);
    }

    @Override
    public CustomApplicationEventListener[] getCustomApplicationEventListeners() {
        return listeners.toArray(new CustomApplicationEventListener[0]);
    }

    @Override
    public CustomApplicationEventListener[] getCustomApplicationEventListeners(Class<? extends CustomApplicationEvent> eventType) {
        return listeners.stream()
                .filter(customApplicationEventListener -> {
                    // 判断当前类的泛型是否为当前类型
                    Type[] genericInterfaces = customApplicationEventListener.getClass().getGenericInterfaces();
                    return Stream.of(genericInterfaces)
                            .anyMatch(genericInterface ->
                                    {
                                        if (genericInterface instanceof ParameterizedType) {
                                            Type rawType = ((ParameterizedType) genericInterface).getRawType();
                                            return CustomApplicationEventListener.class.getName().equals(rawType.getTypeName()) && Stream.of(((ParameterizedType) genericInterface).getActualTypeArguments())
                                                    .anyMatch(type -> eventType.getName().equals(type.getTypeName()));
                                        }
                                        return false;
                                    }
                            );
                }).toArray(CustomApplicationEventListener[]::new);
    }

    /**
     * 自定义监听器比较器
     */
    class CustomListenerComparator implements Comparator<CustomApplicationEventListener> {
        /**
         * 使用listener的实际类名称进行比较
         *
         * @param o1
         * @param o2
         * @return
         */
        @Override
        public int compare(CustomApplicationEventListener o1, CustomApplicationEventListener o2) {
            return o1.getClass().getName().compareTo(o2.getClass().getName());
        }
    }
}
