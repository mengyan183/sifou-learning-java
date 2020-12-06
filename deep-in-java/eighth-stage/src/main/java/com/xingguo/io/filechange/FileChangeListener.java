/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.filechange;

import java.util.EventListener;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

/**
 * FileChangeListener
 * 文件变化监听器
 *
 * @author guoxing
 * @date 2020/12/6 3:27 PM
 * @since
 */
@FunctionalInterface
public interface FileChangeListener extends EventListener,
        Consumer<FileChangeEvent>,
        Observer // jdk9后就被标记为过期了
{
    void onFileChange(FileChangeEvent fileChangeEvent);

    @Override
    default void update(Observable o, Object arg) {
        if (arg instanceof FileChangeEvent) {
            onFileChange((FileChangeEvent) arg);
        }
    }

    @Override
    default void accept(FileChangeEvent fileChangeEvent) {
        onFileChange(fileChangeEvent);
    }
}
