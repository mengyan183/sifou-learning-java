/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.modules;

import java.util.logging.Logger;

/**
 * TransitiveDemo
 * 模块依赖传递
 *
 * @author guoxing
 * @date 9/14/20 8:58 PM
 * @since
 */
public class TransitiveDemo {
    public static void main(String[] args) {
        Logger logger ;// 由于依赖了java.sql中存在 transitive java.logging,因此可以直接使用java.logging中的代码, 而当不直接或间接依赖 java.logging时,当前代码会编译错误
    }
}
