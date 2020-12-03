/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.desgin.pattern;

import java.util.stream.IntStream;

/**
 * CharacterWrapper
 * 装饰器模式/包装器模式
 *
 * @author guoxing
 * @date 2020/12/1 9:25 PM
 * @since
 */
public class CharacterWrapper implements CharSequence {

    private final CharSequence delegate;

    public CharacterWrapper(CharSequence delegate) {
        this.delegate = delegate;
    }

    @Override
    public int length() {
        return delegate.length();
    }

    @Override
    public char charAt(int index) {
        return delegate.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return delegate.subSequence(start, end);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public IntStream chars() {
        return delegate.chars();
    }

    @Override
    public IntStream codePoints() {
        return delegate.codePoints();
    }
}
