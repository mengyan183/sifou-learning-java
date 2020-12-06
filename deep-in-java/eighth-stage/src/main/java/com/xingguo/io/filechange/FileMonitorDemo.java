/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.io.filechange;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * FileMonitorDemo
 * 对于当前文件监听操作实际就是通过判断指定文件的`{@link File#lastModified()}来判断指定文件是否发生变更
 *
 * @author guoxing
 * @date 2020/12/6 3:42 PM
 * @since
 */
public class FileMonitorDemo {
    /**
     * 缓存文件上一次变更时间
     * 对于{@link File#compareTo(File)}中其通过 路径进行比较文件是否相同
     * 对于{@link File#equals(Object)}也是利用了 {@link File#compareTo(File)} 来进行判断
     * 因此可以将{@link File}作为key来保证文件的唯一性
     */
    private Map<File, Long> fileLastModifiedCache = new LinkedHashMap<>();
    // 利用 定时线程实现定时监听
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    // 创建文件变化事件发布
    private FileChangePublisher fileChangePublisher = new FileChangePublisher();

    /**
     * 添加文件变化监听器,要求至少有一个监听器
     *
     * @param fileChangeListener
     * @param fileChangeListeners
     */
    public void addFileChangeListener(FileChangeListener fileChangeListener, FileChangeListener... fileChangeListeners) {
        fileChangePublisher.addFileChangeListener(Objects.requireNonNull(fileChangeListener));
        if (Objects.nonNull(fileChangeListeners)) {
            Stream.of(fileChangeListeners)
                    .forEach(fileChangePublisher::addFileChangeListener);
        }
    }

    public void execute(File file) {
        Objects.requireNonNull(file);
        // 通过定时线程去探测文件变化,并不能保证及时性
        executorService.scheduleAtFixedRate(() -> {
            Long elderLastModified = fileLastModifiedCache.get(file);
            long lastModified = file.lastModified();
            // 如果文件最终修改时间发生变化,说明文件发生修改,则发布文件变更通知
            if (Objects.isNull(elderLastModified) || elderLastModified < lastModified) {
                System.out.printf("elder:%s;last:%s\n", elderLastModified, lastModified);
                fileChangePublisher.publish(file);
            }
            // 新增/修改最新文件变更时间
            fileLastModifiedCache.put(file, lastModified);
        }, 0, 5, TimeUnit.SECONDS);

    }

    public static void main(String[] args) {
        FileMonitorDemo fileMonitorDemo = new FileMonitorDemo();
        fileMonitorDemo.addFileChangeListener(fileChangeEvent -> {
            System.out.println("处理文件变更事件:" + fileChangeEvent);
        });
        String targetPath = fileMonitorDemo.getClass().getClassLoader().getResource("").getPath();
        fileMonitorDemo.execute(new File(targetPath));
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        String randomName = UUID.randomUUID().toString();
        executorService.scheduleAtFixedRate(() -> {
            File file = new File(targetPath + "/" + randomName + ".text");
            if (file.exists()) {
                file.delete();
                System.out.println("删除文件");
            } else {
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    fileOutputStream.flush();
                    System.out.println("生成文件");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

}
