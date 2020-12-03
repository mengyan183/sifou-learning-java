/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.desgin.pattern;

import java.util.stream.IntStream;

/**
 * DelegatingCharacter
 * 装饰器模式/包装器模式
 * 对于装饰器/包装模式,其特点在于只会扩展一些非重要(并非无意义)的功能(方法),对于其核心仍然来自于其继承的类或实现的接口,而wrapper/decorator(包装/装饰)则来自于外部注入,并非内部实现
 *
 * @author guoxing
 * @date 2020/12/1 9:20 PM
 * @since
 */
public class DecoratingCharacter implements CharSequence {

    private final CharSequence decorator;

    public DecoratingCharacter(CharSequence decorator) {
        this.decorator = decorator;
    }

    /**
     * 委派{@link CharSequence} 全部的方法
     */


    @Override
    public int length() {
        return decorator.length();
    }

    @Override
    public char charAt(int index) {
        return decorator.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return decorator.subSequence(start, end);
    }

    @Override
    public String toString() {
        return decorator.toString();
    }

    @Override
    public IntStream chars() {
        return decorator.chars();
    }

    @Override
    public IntStream codePoints() {
        return decorator.codePoints();
    }

    public String getDescription(){
        return toString();
    }
}
