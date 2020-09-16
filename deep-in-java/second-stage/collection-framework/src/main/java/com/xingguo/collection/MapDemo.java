/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection;

import java.util.Map;
import java.util.Objects;

/**
 * MapDemo
 * map作为缓存数据
 *
 * @author guoxing
 * @date 9/16/20 2:49 PM
 * @since
 */
public class MapDemo {
    public static void main(String[] args) {
        // 创建一个只读map
        Map<Integer, String> map = Map.of(1, "A");
        // 对于缓存数据map一般推荐使用 Number作为key进行数据存储
        // 在 map的contains和get等操作中
        // 在map的compare操作中实际是利用到了hashcode,对于Integer中的hashcode实际就是当前值,因此数据对比操作更加高效
        System.out.println(map.get(1));
        // 由于在比较操作时实际都是用到了 equal和hashcode方法
        // 因此在调用get方法时实际可以传入任意类型的数据,只要equal方法满足条件集合
        System.out.println(map.get(1L));
        System.out.println(map.get(1.0f));
        // 由于在get方法中一般都是使用的equals方法和hashcode方法,因此只要保证这两个方法符合条件,因此在get操作时可以使用任意类型的数据
        // 且对于Integer类型的数据由于其equals 和 hashcode方法的简单性,因此只要其他自定义类型(包装了int类型)重写了equals和hashcode,就可以达到和Integer类型一样的效果
        System.out.println(map.get(new Key(1)));
    }

    public static class Key {
        private final int key;

        public Key(int key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof Integer) {
                return this.key == (Integer) o;
            }
            Key key1 = (Key) o;
            return key == key1.key;
        }

        @Override
        public int hashCode() {
            return this.key;
        }
    }
}
