/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.nio;

import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * AtomicOperateDemo  文件原子操作
 * {@link java.nio.file.AtomicMoveNotSupportedException}
 * {@link java.nio.file.Files#move(Path, Path, CopyOption...)} 对于当前操作实际允许支持原子操作
 * <p>
 * {@link Files#copy(java.nio.file.Path, java.nio.file.Path, java.nio.file.CopyOption...)}同样利用到了 {@link java.nio.file.CopyMoveHelper}也存在原子操作
 *
 * @author guoxing
 * @date 2020/12/8 2:50 PM
 * @since
 */
public class AtomicOperateDemo {
}
