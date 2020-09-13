/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first.method;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * MethodNameDesignDemo
 * 方法名称命名
 *
 * @author guoxing
 * @date 9/13/20 9:18 PM
 * @since
 */
public class MethodNameDesignDemo {

    public static void main(String[] args) {
        // 动词 + 形容词
        Stream.of(1, 2).forEachOrdered(System.out::println);
    }

    public class ViewRender { // 名词

        public void render() { // 单一动词

        }

        // 同步渲染
        public void renderSynchronously() { // 动词+副词

        }

        // 并发渲染
        public void renderConcurrently() { // 动词+副词

        }

        public List<String> listValuesSynchronously() { // 动词+名词+副词
            return Collections.emptyList();
        }
    }
}
