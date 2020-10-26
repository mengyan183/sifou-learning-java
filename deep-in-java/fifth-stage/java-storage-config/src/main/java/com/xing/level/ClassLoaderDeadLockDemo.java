/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xing.level;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassLoaderDeadLockDemo
 * 类加载死锁的问题
 *
 * @author guoxing
 * @date 2020/10/26 9:19 AM
 * @since
 */
@Slf4j
public class ClassLoaderDeadLockDemo {
    // 定义一个常量 ,在类加载时期写入到 constant-pool中
    public static final Object OBJECT = new Object();

    static {
        log.info("加载执行静态代码块");
        log.info("{}", System.identityHashCode(Thread.currentThread().getContextClassLoader()));
        // TODO : case 1
        // lambda 表达式
        // 当对于 class文件使用 javap -v 可以看到其中 有一个 InvokeDynamic jvm指令,这里是对lambda 语法的字节码提升
        // invokedynamic #64,  0             // InvokeDynamic #0:run:()Ljava/lang/Runnable;
        // 而由于此处未使用外部类,而是依赖于当前类,因此会产生类加载依赖
        // 当 ClassLoaderDeadLockDemo 类加载和初始化执行到该处时调用 invokedynamic,而 invokedynamic 此时又依赖于当前ClassLoaderDeadLockDemo的加载完毕,此时就出现了依赖的循环,从而导致了线程阻塞
        // 对于此处是动态生成的而不是和匿名内置类一样会静态生成class文件
        // 对于ClassLoaderDeadLockDemo.class而言, InvokeDynamic指令属于ClassLoaderDeadLockDemo类加载的一部分, 而 对于 InvokeDynamic run 实际又依赖于ClassLoaderDeadLockDemo的加载完成
        // 因此此处就形成了类加载的依赖循环
        Thread thread = new Thread(() -> {
//            log.info("{}线程执行", Thread.currentThread().getName());
            // 引用外部类的共享变量
//            Object o = ClassLoaderDeadLockDemo.OBJECT;
        });
        // TODO : case 2
        // 采用匿名内置类的形式 等价于 lambda表达式
        // 当编译之后可以看到会生成一个 ClassLoaderDeadLockDemo$1.class文件
        // 此时使用 javap -v 解析class文件 可以看到

        /**
         *  new           #46                 // class com/xing/level/ClassLoaderDeadLockDemo$1
         *
         * NestMembers:
         *   com/xing/level/ClassLoaderDeadLockDemo$1
         * InnerClasses:
         *   #46;                                    // class com/xing/level/ClassLoaderDeadLockDemo$1
         */
        // 在这里可以看到 通过 new 关键字 来创建匿名内置类(Runnable接口的实现类)的实例
        /**
         * 从类加载的顺序进行分析
         * ClassLoader#load 首先会加载 com.xing.level.ClassLoaderDeadLockDemo class文件
         * 当解析到  Thread thread = new Thread(new Runnable() { ....});时
         * 此时就会加载 com.xing.level.ClassLoaderDeadLockDemo$1 class文件
         * Question1:是否是死锁?
         * 虽然 load是加锁的,但两个类加载都是使用的相同的线程,且根据 synchronized 的可重入原则;因此其不是死锁的问题
         *
         * 当执行到   thread.start(); 时,com.xing.level.ClassLoaderDeadLockDemo$1已经加载完毕
         * 此时继续往后执行, 当执行到 thread.join(),此时由于内部调用了 Object#wait 会阻塞当前main线程,由于当前com.xing.level.ClassLoaderDeadLockDemo 尚未加载完毕因此对其内置类中的方法永远无法执行;所以 main线程就被阻塞
         * 在 thread.join(); 阻塞不会继续往后执行
         */
//        Thread thread = new Thread(new Runnable() {
//            {
//                // 通过打印结果可以看到 当前匿名内置类的类加载器和外部类的类加载器是使用的相同的类加载器
//                log.info("{}", System.identityHashCode(Thread.currentThread().getContextClassLoader()));
//                Object o = ClassLoaderDeadLockDemo.OBJECT;
//                log.info("{}", o);
//            }
//
//            @Override
//            public void run() {
//                // 当 run 中没有任何依赖外部类代码时,该进程也会执行结束
//                System.out.println("内置类运行");
////                log.info("内置类运行");
////                log.info("{}线程执行", Thread.currentThread().getName());
////                // 引用外部类的共享变量
////                Object o = ClassLoaderDeadLockDemo.OBJECT;
//            }
//        });

        //TODO : case3 方法引用
        /**
         * 编译后(javap -v )的结果可以看到这里是引用的PrintStream类的方法数据
         *  #74 = InvokeDynamic      #0:#75        // #0:run:(Ljava/io/PrintStream;)Ljava/lang/Runnable;
         *  由于引用的是外部类和当前类加载无关,因此不会出现类加载死锁的问题
         */
//        Runnable target = System.out::println;
//        Runnable target = HelloWorld::helloWorld;
//        Thread thread = new Thread(target);
        thread.start();
        try {
            /**
             * 当运行后 通过 jps 搭配 jstack pid 查看 当前jvm进程线程信息可以看到
             * // 当前main线程被阻塞住 阻塞在 thread.join();处
             *  "main" #1 prio=5 os_prio=31 cpu=138.23ms elapsed=17.64s tid=0x00007f982c00da00 nid=0x2603 in Object.wait()  [0x0000700007d9c000]
             *    java.lang.Thread.State: WAITING (on object monitor)
             *         at java.lang.Object.wait(java.base@15/Native Method)
             *         - waiting on <0x000000070f83e6a8> (a java.lang.Thread)
             *         at java.lang.Thread.join(java.base@15/Thread.java:1303)
             *         - locked <0x000000070f83e6a8> (a java.lang.Thread)
             *         at java.lang.Thread.join(java.base@15/Thread.java:1371)
             *         at com.xing.level.ClassLoaderDeadLockDemo.<clinit>(java.storage.config/ClassLoaderDeadLockDemo.java:74)
             * // 显示当前异步线程在回调 start中的run
             *  "Thread-0" #22 prio=5 os_prio=31 cpu=0.07ms elapsed=17.50s tid=0x00007f982b04e000 nid=0x9103 in Object.wait()  [0x00007000090d8000]
             *    java.lang.Thread.State: RUNNABLE
             *         at com.xing.level.ClassLoaderDeadLockDemo$1.run(java.storage.config/ClassLoaderDeadLockDemo.java:66)
             *         - waiting on the Class initialization monitor for com.xing.level.ClassLoaderDeadLockDemo
             *         at java.lang.Thread.run(java.base@15/Thread.java:832)
             */
            // 当取消当前代码时,当前进程就会正常执行结束
            thread.join(); // main 线程执行到此处, main线程被阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * [main] INFO com.xing.level.ClassLoaderDeadLockDemo - 加载执行静态代码块
     * 根据运行结果可以看到在加载执行静态代码块时就被阻塞了; 因此当前代码不是在运行时出现问题,而是在类加载出现了问题
     * 对于 TODO : 1 没有任何解决方法
     * TODO : 2 和 TODO : 3 可以通过不依赖当前类的相关数据也可以实现 运行通过
     *
     * @param args
     */
    public static void main(String[] args) {
        log.info("{}线程执行", Thread.currentThread().getName());
    }
}
