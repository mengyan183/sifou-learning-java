/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.bytecode.dynamicproxy;

/**
 * NameableProxyService
 * 为了支持jdk 基于接口的动态代理实现, {@link com.xingguo.bytecode.beans.ProxyPojo}
 * 通过 jdk 动态代理 实现 {@link java.beans.PropertyChangeEvent} 相关操作
 *
 * @author guoxing
 * @date 2020/11/28 4:25 PM
 * @since
 */
public interface NameableProxyService {
    /**
     * 设置 {@link com.xingguo.bytecode.beans.ProxyPojo#name} 操作
     *
     * @param name 名称
     */
    void setName(String name);

}
