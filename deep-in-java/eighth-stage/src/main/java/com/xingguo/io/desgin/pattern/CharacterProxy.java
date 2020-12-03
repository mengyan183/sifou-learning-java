/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.desgin.pattern;

/**
 * CharacterProxy
 * 代理模式简述
 * 对于代理对象和被代理对象,并不一定存在直接的层次或继承关系
 * 且对于代理对象中的功能一般为被代理对象功能的子集
 * <p>
 * A类型提供了N个方法
 * 对于 decoratedA 是继承了 A
 * 对于 delegatedA is A
 * 对于 decoratedA 并不一定完全按照A去运行
 *
 * @author guoxing
 * @date 2020/12/1 9:15 PM
 * @since
 */
public class CharacterProxy {
    // delegate 被代理
    private final CharSequence delegate;

    public CharacterProxy(CharSequence delegate) {
        this.delegate = delegate;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
