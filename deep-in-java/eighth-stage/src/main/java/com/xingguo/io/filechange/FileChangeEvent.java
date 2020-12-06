/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.filechange;

import java.io.File;
import java.util.EventObject;

/**
 * FileChangeEvent
 * 文件变化事件监听
 *
 * @author guoxing
 * @date 2020/12/6 3:25 PM
 * @since
 */
public class FileChangeEvent extends EventObject {

    public FileChangeEvent(File file) {
        super(file);
    }


    @Override
    public File getSource() {
        return (File) super.getSource();
    }
}
