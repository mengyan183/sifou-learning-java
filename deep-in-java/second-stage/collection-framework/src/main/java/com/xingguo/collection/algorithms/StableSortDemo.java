/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.algorithms;

import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

/**
 * StableSortDemo
 * 稳定排序
 *
 * @author guoxing
 * @date 9/19/20 10:50 AM
 * @since
 */
public class StableSortDemo {

    public static void main(String[] args) {
        LinkedMultiValueMap<Integer, String> integerStringLinkedMultiValueMap = new LinkedMultiValueMap<>();
        // 保证了排序的稳定性
        // 对于相同的数据采用连表进行存储,保证了相同的数据,写入前的顺序和写入后的顺序一致
        while (true) {
            integerStringLinkedMultiValueMap.clear();
            integerStringLinkedMultiValueMap.add(5, "红");
            integerStringLinkedMultiValueMap.add(5, "黑");
            String first = integerStringLinkedMultiValueMap.getFirst(5);
            if ("黑".equals(first)) {
                break;
            }
        }
    }
}
