/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.bytecode.beans;

import com.xingguo.bytecode.dynamicproxy.NameableProxyService;

import java.io.Serializable;

/**
 * ProxyPojo
 * 测试动态代理 的pojo
 *
 * @author guoxing
 * @date 2020/11/28 4:24 PM
 * @since
 */
public class ProxyPojo implements Serializable, NameableProxyService {

    private String name;

    @Override
    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ProxyPojo{" +
                "name='" + name + '\'' +
                '}';
    }
}
