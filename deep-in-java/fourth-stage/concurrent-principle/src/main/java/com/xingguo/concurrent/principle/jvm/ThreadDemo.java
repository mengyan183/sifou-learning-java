/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.principle.jvm;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadDemo
 * 这里的代码都是基于 jdk15
 *
 * @author guoxing
 * @date 2020/10/14 9:42 PM
 * @since
 */
@Slf4j
public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        threadApi();
    }

    public static void threadApi() throws InterruptedException {
        /**
         * 在加载Thread.class 到jvm中时会执行当前Thread 类中的static代码块 以及 static变量
         * 此时就会执行 registerNatives 的native 方法对应的就是 thread.c 中的 Java_java_lang_Thread_registerNatives 方法;
         * 该方法的执行会将Thread中所有映射的native 方法和变量进行加载到jvm中
         */
        // 创建一个线程对象,对于这里的操作只涉及到java层面的对象创建,并不涉及os线程操作;对于其中的属性操作
        //  target -> 当前线程对象指定的runnable方法
        // group -> 线程组: 对于一个新的线程对象而言,默认会使用当前调用线程的所属线程组;
        // 对于stackSize 属性在@since 1.4 后就可以人工指定,而不需要只使用 xss的限制
        Thread thread = new Thread(() -> {
            log.info("java.lang.Thread api 学习");
        });

        // 启动线程 : thread#start
        /**
         *  start 首先是被synchronized 修饰表示当前方法操作是线程安全的同步操作: 因此就可以理解java language 规范中 happens-before 对于 start 和 run 的定义
         *  在start 中 首先会将当前线程对象添加到thread.threadGroup; 然后会调用 start0(native)
         *   对于start0是属于JNI方法,其对应的是jdk源码中的c++代码对应的就是"JVM_StartThread" 其对应的实现就是jvm.cpp中的 JVM_ENTRY(void, JVM_StartThread(JNIEnv* env, jobject jthread)) 方法;
         *   "java_lang_Thread::thread(JNIHandles::resolve_non_null(jthread)) != NULL"  对于 当前判断是通过判断当前jobject 是否已被实例化,如果已经被实例化说明当前对象已被创建表示对应的线程已被创建; 因此对于一个Thread对象而言如果调用了多次start方法则会抛出 异常"java_lang_IllegalThreadStateException"-> 对应的就是 "java.lang.IllegalThreadStateException" 在openjdk中 使用了 "THROW(vmSymbols::java_lang_IllegalThreadStateException());" 来抛出异常,其映射是存在于 vmSymbols.h中的模板定义"  template(java_lang_IllegalThreadStateException,     "java/lang/IllegalThreadStateException")    \"
         *
         *  "native_thread = new JavaThread(&thread_entry, sz);" 此时会创建 JavaThread 对象, 在这里传递了两个参数一个是 "thread_entry方法的引用(类似于java lambda)",第二个参数为当前线程栈大小
         *  对 "thread_entry"方法的分析:
         *      static void thread_entry(JavaThread* thread, TRAPS) {
         *   HandleMark hm(THREAD);
         *   Handle obj(THREAD, thread->threadObj()); // 该操作就是为了java Thread 对象
         *   JavaValue result(T_VOID);
         *   JavaCalls::call_virtual(&result, // 对于 call_virtual 实际就是执行的回调操作,该操作会调用java Thread#run 方法
         *                           obj,
         *                           SystemDictionary::Thread_klass(),
         *                           vmSymbols::run_method_name(), // 此处就是将对应的 run方法加入其中
         *                           vmSymbols::void_method_signature(),
         *                           THREAD);
         * }
         *  此时进入 JavaThread::JavaThread(ThreadFunction entry_point, size_t stack_sz) 构造方法
         *      1:执行当前JavaThread 对象的初始化操作:initialize()
         *      2:判断当前线程类型时 java_thread 还是 compiler_thread, 此时会调用 os::create_thread(Thread* thread, ThreadType thr_type, size_t req_stack_size)
         *  此时进入 os::create_thread(Thread* thread, ThreadType thr_type, size_t req_stack_size) (os_linux.cpp):
         *      在当前代码的关键点就在于 pthread api的使用,利用POSIX_THREAD高级api来创建os线程;
         *      对于 pthread_create第三个参数传递的 thread_native_entry 方法引用分析
         *          重点在于对于 "thread->call_run();"通过调用 "JavaThread::run()" -> "JavaThread::thread_main_inner()" -> "this->entry_point()(this, this);" (这里可以看上面"thread_entry"的解释) ->  该操作是正式执行Thread#run回调;
         *          当当前线程执行结束后会执行"JavaThread::post_run()" 进行资源回收
         *      对于当前方法而言,有可能由于内存不足导致线程创建失败,通过
         *      // Allocate the OSThread object
         *   OSThread* osthread = new OSThread(NULL, NULL);
         *   if (osthread == NULL) {
         *     return false;
         *   }
         *   来判断是否有空间创建线程对象;如果空间分配失败则表示线程创建失败,但其不会抛出OOM异常
         * 对于 以下代码分析:
         *  if (native_thread->osthread() != NULL) {
         *         // Note: the current thread is not being used within "prepare".
         *         native_thread->prepare(jthread);
         *       }
         *   osthread() 实际是调用的 "thread.hpp中的   OSThread* osthread() const                     { return _osthread;   }"
         *   对于 os::create_thread 中如果OSThread 对象创建成功则会调用 "void set_osthread(OSThread* thread)            { _osthread = thread; }",来设置 "_osthread" 属性,因此当对象OSThread 创建失败时,当前字段为null,因此可以通过当前字段判断线程是否真正创建成功
         */
        thread.start();
//        thread.start();// 抛出 IllegalThreadStateException

        /**
         * 阻塞等待当前线程执行结束
         * 这里主要是通过调用 native java.lang.Thread#isAlive() 实现,对应的c++ 中的操作就是去获取JavaThread 对象是否存在,对于 thread#start 中 在c++中有一个操作就是在post_run方法中会进行资源回收,因此当c++中javaThread执行结束后,对象就会被资源回收,代表当前线程已经执行结束;
         *
         * 这里还有一点关于 Object#wait native 分析: 对于wait操作实际是直接执行膨胀锁并不会有偏向锁的存在
         * 对于 Object#wait 对应的 jni 定义 存在于 jvm.h 的
         * JNIEXPORT void JNICALL
         * JVM_MonitorWait(JNIEnv *env, jobject obj, jlong ms);
         * 其具体实现为 jvm.cpp 中的 "JVM_ENTRY(void, JVM_MonitorWait(JNIEnv* env, jobject handle, jlong ms))" 主要为 调用 "ObjectSynchronizer::wait(obj, ms, CHECK);"
         * 此时进入 synchronizer.cpp中的
         * int ObjectSynchronizer::wait(Handle obj, jlong millis, TRAPS)
         *  对于当前方法首先判断JVM是否支持偏向锁,如果支持偏向锁,"BiasedLocking::revoke(obj, THREAD);" 会对当前对象头执行偏向锁撤销操作
         * 后续会直接调用 "ObjectMonitor* ObjectSynchronizer::inflate(Thread* self, oop object,const InflateCause cause)" 进入膨胀过程
         *  对于膨胀的过程实际就是对 Inflated(已持有到锁) -> Stack-locked -> INFLATING -> Neutral -> BIASED 按照锁的粒度从高到低进行判断,最终转换为膨胀锁阶段 返回 ObjectMonitor;
         *  对于当前inflate实际就是 object转换为 ObjectMonitor的过程
         *  对于 ObjectMonitor分析 : 其中存在一个 waitSet 属性值, 其操作就是类似于 AQS 的双向队列
         *  ObjectWaiter* volatile _WaitSet;  // LL of threads wait()ing on the monitor
         *  对于 ObjectWaiter 也可以等价于 java.util.concurrent.locks.AbstractQueuedSynchronizer.Node
         * 再次进入 objectMonitor.cpp中的
         * void ObjectMonitor::wait(jlong millis, bool interruptible, TRAPS)
         *  会首先创建一个ObjectWaiter 节点准备加入到等待队列中,会使用当前Thread的parkevent 执行reset进行重置挂起操作,使用OrderAccess.fence建立内存屏障
         *  对于addWaiter 操作也是受到 spinlock的保护,因此需要 在入队之前先执行 Thread::SpinAcquire 操作 对于当前入队操作 和 java.util.concurrent.locks.AbstractQueuedSynchronizer#acquire(java.util.concurrent.locks.AbstractQueuedSynchronizer.Node, int, boolean, boolean, boolean, long) 操作类似, 不同点在于 对于 c++是直接使用了 spinLock;
         *  对于 spinLock acquire 首先会执行cas操作作为fast,如果失败,则执行下一个阶段, 在此阶段如果为单核则直接阻塞当前线程; 反之则 会首先执行 线程 yield ,并执行cas操作如果操作持续失败,且yield次数达到阈值,则会执行线程休眠1ms操作,并直到cas操作成功,则加锁成功;
         *  在执行AddWaiter 操作中 和 aqs node acquire不同点在于初始化链表时并不会创建一个无意义的头节点; 释放锁
         * 调用 Thread#ParkEvent属性 的park 方法 实际 就是 os::PlatformEvent::park 内部使用了 POSIXThread中的相关的lock以及condition 相关api
         *
         */
        thread.join();

        /**
         * 分析 jdk.internal.misc.Unsafe#park(boolean, long)
         * 对应的就是 unsafe.cpp中的 "UNSAFE_ENTRY(void, Unsafe_Park(JNIEnv *env, jobject unsafe, jboolean isAbsolute, jlong time))" 其最终操作也是执行到了os::PlatformEvent::park 内部使用了 POSIXThread中的相关的lock以及condition 相关api
         */
    }

    public static void executors() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // 由于 core = 2, 当前只提交了一个任务,因此会理解创建任务,并创建新的thread对象,并调用start 方法来执行当前线程的runnable
        executorService.submit(() -> {
            log.info("线程启动");
        });
        executorService.shutdown();
    }
}
