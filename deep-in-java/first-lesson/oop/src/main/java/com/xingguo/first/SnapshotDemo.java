/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SnapshotDemo
 * 快照数据
 *
 * @author guoxing
 * @date 9/11/20 9:42 PM
 * @since
 */
public class SnapshotDemo {

    public static void main(String[] args) {
        Data data = new Data();
        data.setStrings(Arrays.asList("1", "2"));
        // 对于这里就将内部状态暴露对外,可能会导致内部数据就被修改
        List<String> strings = data.getStrings();


    }

    public static class Data {
        private List<String> strings;
        // 实际是将内部数据暴露对外
        public List<String> getStrings() {
            return strings;
        }

        public void setStrings(List<String> strings) {
            this.strings = strings;
        }
    }


    public static class SnapShotData {
        private List<String> strings;

        // 对于此处返回的数据实际就是一个新的对象数据, 不过这里实际也是浅拷贝
        // 对于String 而言本身就是不可修改的
        public List<String> getStrings() {
            return new ArrayList<>(strings);
        }

        public void setStrings(List<String> strings) {
            this.strings = strings;
        }
    }

}
