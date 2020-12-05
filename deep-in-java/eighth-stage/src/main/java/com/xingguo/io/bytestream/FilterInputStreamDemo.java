/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.bytestream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FilterInputStreamDemo
 *
 * @author guoxing
 * @date 2020/12/3 1:21 PM
 * @since
 */
public class FilterInputStreamDemo {
    /**
     * 对于{@link FilterInputStream} 作为{@link InputStream} 装饰器模式 的实现类
     * 根据其作用可以看出 {@link FilterInputStream} 继承重写了 {@link InputStream}的全部的方法,并通过支持外部注入 {@link InputStream}具体实现的方式利用多态的特性
     * 对于 {@link FilterInputStream} 中 Filter 的理解 根据其注释可以看出 其是为了方便需要拓展 {@link InputStream} 的类通过 继承 {@link FilterInputStream} 来实现自定义忽略(不需要实现全部的功能)或增强(新增新的功能)
     * 因此 Filter 是体现在其子类可以过滤不需要重写的方法,而在具体使用层面并不存在过滤的特性
     * <p>
     * 对于Filter 更类似于委派模式,其实际内部全部的实现都委派给了{@link InputStream}
     */
    public static void main(String[] args) {
        CustomInputStream customInputStream = new CustomInputStream(System.in);
    }
}

class CustomInputStream extends FilterInputStream {

    protected CustomInputStream(InputStream in) {
        super(in);
    }

    /**
     * Reads the next byte of data from this input stream. The value
     * byte is returned as an <code>int</code> in the range
     * <code>0</code> to <code>255</code>. If no byte is available
     * because the end of the stream has been reached, the value
     * <code>-1</code> is returned. This method blocks until input data
     * is available, the end of the stream is detected, or an exception
     * is thrown.
     * <p>
     * This method
     * simply performs <code>in.read()</code> and returns the result.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     * stream is reached.
     * @throws IOException if an I/O error occurs.
     * @see FilterInputStream#in
     */
    @Override
    public int read() throws IOException {
        // 对于 filter 的存在 就是体现在可以自主选择是否需要重写
        return super.read();
    }
}

/**
 * 自定义FilterInputStream
 */
class CustomFilterInputStream extends InputStream {
    // 被装饰者
    private final InputStream decorator;

    public CustomFilterInputStream(InputStream decorator) {
        this.decorator = decorator;
    }

    @Override
    public int read() throws IOException {
        return decorator.read();
    }


}
