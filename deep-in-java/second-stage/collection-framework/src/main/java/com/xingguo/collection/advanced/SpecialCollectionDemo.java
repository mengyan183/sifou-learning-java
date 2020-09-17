/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.collection.advanced;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.concurrent.TimeUnit;

/**
 * SpecialCollectionDemo
 * 特殊的集合类型
 *
 * @author guoxing
 * @date 9/17/20 2:21 PM
 * @since
 */
public class SpecialCollectionDemo {
    public static void main(String[] args) throws Throwable {
        WeakHashMapDemo weakHashMapDemo = new WeakHashMapDemo();
        weakHashMapDemo.weakHashMap();

        ThreadLocalDemo threadLocalDemo = new ThreadLocalDemo();
        threadLocalDemo.threadLocalMap();

        IdentityHashMapDemo identityHashMapDemo = new IdentityHashMapDemo();
        identityHashMapDemo.sameValue();
        identityHashMapDemo.sameValueIdentityHashMap();

        System.out.println("===========");

        identityHashMapDemo.identityHashCodeVsHashCode();
    }
}

class IdentityHashMapDemo {

    public void identityHashCodeVsHashCode() throws Throwable {
        Object o = new Object();
        // 对于Object对象 而言;System.identityHashCode 和 Object#hashCode是相同的
        assert System.identityHashCode(o) == o.hashCode();
        // 对于 s1 和 s2 实际都是引用的 META区域中相同的对象
        String s1 = "1";
        String s2 = "1";
        assert System.identityHashCode(s1) == System.identityHashCode(s2);

        String s3 = new String("1");
        assert s1.hashCode() == s3.hashCode();
        // 对于s1 和 s3 实际是两个不同的对象, 但由于 String重写了Object#hashCode()
        // 当需要对比对象时,我们并不能直接调用到Object#hashCode(),因此只能通过System.identityHashCode来获取对象的hashcode
        assert System.identityHashCode(s1) != System.identityHashCode(s3);
        // 获取指定类的构造方法并执行
        Object object = MethodHandles.lookup().findConstructor(Object.class, MethodType.methodType(void.class)).invoke();
        // 获取指定类的方法并绑定指定的对象
        MethodHandle methodHandle = MethodHandles.lookup().findVirtual(Object.class, "hashCode", MethodType.methodType(int.class)).bindTo(s3);
        Object invoke = methodHandle.invoke();
        System.out.println((int)invoke);
        assert (int)invoke == System.identityHashCode(s3);
    }

    public void sameValueIdentityHashMap() {
        // 对于IdentityHashMap其key的hash方法是自定义的hash方法,而非使用的key的类型的hash方法
        IdentityHashMap<String, String> stringStringIdentityHashMap = new IdentityHashMap<>();
        stringStringIdentityHashMap.put("A", "A");
        stringStringIdentityHashMap.put(new String("A"), "A");
        System.out.println(stringStringIdentityHashMap.size()); // 2

    }

    public void sameValue() {
        // 对于hashMap实际是使用的key的hashcode方法进行计算
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("A", "A");
        stringStringHashMap.put(new String("A"), "A");
        System.out.println(stringStringHashMap.size());
        // 对于String数据而言,对于字面常量和对象而言,虽然对于数据而言逻辑相等,但对象不相等
        System.out.println("A".equals(new String("A")));
        System.out.println("A" == new String("A"));
    }

}

class ThreadLocalDemo {
    /**
     * ThreadLocal 中的ThreadLocalMap实际是和线程绑定到一起的,当线程消亡后,ThreadLocalMap中对应线程的数据则会被自动回收掉
     */
    public void threadLocalMap() throws InterruptedException {
        ThreadLocal<User> stringThreadLocal = new ThreadLocal<>();
        Runnable runnable = () -> {
            stringThreadLocal.set(new User("A"));
        };
        runnable.run();
        System.gc();
        Thread.sleep(1000L);

        System.out.println(stringThreadLocal.get());
    }
}

class WeakHashMapDemo {
    public void weakHashMap() throws InterruptedException {
        // 强引用
        // 对于字符串字面量而言,其是存在于jvm META区域
        // 而对于 value 变量存在于栈中,当当前方法执行结束后,该value变量就会被回收;但对于META中的数据只有当ClassLoader被回收掉之后,存在于META中的数据才会给回收掉
        String value = "demo";
        System.gc();
        // 即使手动gc,也不会导致当前变量被回收
        System.out.println(value);

        // 对弱引用而言,对于弱引用对象,当发生gc时,当前弱引用的对象就会被回收掉
        WeakReference<User> userWeakReference = new WeakReference<>(new User("guoxing"));
        System.out.println(userWeakReference.get());

        System.gc();

        Thread.sleep(1000L);
        System.out.println(userWeakReference.get()); // null
    }


}

class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}