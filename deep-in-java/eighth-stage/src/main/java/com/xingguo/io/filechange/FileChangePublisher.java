/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.filechange;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Stream;

/**
 * FileChangePublisher
 *
 * @author guoxing
 * @date 2020/12/6 3:33 PM
 * @since
 */
public class FileChangePublisher extends Observable {
    /**
     * 这里利用Observe观察者模式进行相关的监听操作
     */
    /**
     * 添加文件更新监听器
     *
     * @param fileChangeListener
     */
    public void addFileChangeListener(FileChangeListener fileChangeListener) {
        super.addObserver(fileChangeListener);
    }

    /**
     * 发布事件
     *
     * @param fileChangeEvent
     */
    public void publish(FileChangeEvent fileChangeEvent) {
        super.setChanged();
        super.notifyObservers(fileChangeEvent);
    }

    /**
     * 直接通过文件来创建一个{@link FileChangeEvent}
     *
     * @param file
     */
    public void publish(File file) {
        publish(new FileChangeEvent(file));
    }
}
