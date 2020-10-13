/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.principle.jmm;

/**
 * HappensBeforeDemo
 *
 * @author guoxing
 * @date 2020/10/13 5:13 PM
 * @since
 */
public class HappensBeforeDemo {
    public static void main(String[] args) {

    }

    /**
     * If x and y are actions of the same thread and x comes before y in program order,
     * then hb(x, y)
     */
    public void singleThreadProgramOrder() {
        // action 1
        // action 2

        // 虽然 int a = 0 和 int b = 0 ; 但这两个操作并不存在关联关系,因此就没有所谓的happens-before关系存在
        // 假设存在 int c = a; 对于 int a = 0,对于变量a 而言,存在数据共享的关系因此 则存在happens-before 关系
        int a = 0;
        int b = 0;
    }

    public static class Demo {

    }

    /**
     * There is a happens-before edge from the end of a constructor of an object to the
     * start of a finalizer (§12.6) for that object.
     */
    public void constructorBeforeFinalize() {
        Demo demo = new Demo();
        // 对于 finalize 而言,是在gc时才会被执行的,对于gc操作而言,肯定需要等对象完全实例化后才能被gc,不会存在尚未实例化的对象被gc

    }

    /**
     * If an action x synchronizes-with a following action y, then we also have hb(x, y).
     */
    public void synchronizeRelation() {

    }

    public final static Object monitor = new Object();

    /**
     * The wait methods of class Object (§17.2.1) have lock and unlock actions
     * associated with them; their happens-before relationships are defined by these
     * associated actions
     *
     * @throws InterruptedException
     */
    public static void synchronizeHb() throws InterruptedException {

        /**
         * 该顺序实际为 synchronized(monitor)(monitor.enter) -> monitor.wait -> monitor.exit
         * 当前顺序是由synchronize-with来进行保证,由于 synchronize-with 包含了 happens-before 因此其具有happens-before关系
         */
        synchronized (monitor) {
            // 对于monitor wait 操作要求其必须持有当前对象所有权才能操作,否则会抛出 IllegalMonitorStateException
            monitor.wait(100);
        }
    }
}
