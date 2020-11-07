/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.principle;

/**
 * SynchronizerDemo
 *
 * @author guoxing
 * @date 2020/10/16 3:30 PM
 * @since
 */
public class SynchronizerDemo {
    /**
     * 当调用当前静态同步方法时, 该锁是锁定在当前 SynchronizerDemo.class上
     */
    public synchronized static void staticMethod() {

    }

    /**
     * 当使用当前类实例调用当前方法时, 会锁定调用该方法的对象实例
     */
    public synchronized void objectMethod(){

    }

    Object LOCK = new Object();

    public void customMethod(){
        // 锁定当前自定义对象 LOCK
        synchronized (LOCK){

        }
    }
}
