/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.properties;

import lombok.extern.slf4j.Slf4j;

/**
 * PersonPropertyDemo
 *
 * @author guoxing
 * @date 2020/11/24 11:38 AM
 * @since
 */
@Slf4j
public class PersonPropertyDemo {

    public static void main(String[] args) throws InterruptedException {
        // 对于当前线程捕获异常而言,只会捕获到异常,但并不能恢复当前线程; 因此对于同一个线程抛出异常后,当前线程就会结束,并不会继续向后执行
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            log.error("当前线程捕获到的异常信息", e);
        });
        Person person = new Person();
        // 增加事件监听器
        person.addPropertyChangeListener(event -> {
            log.info("source:{}中的属性:{}发生变化,oldVal为{},newVal为{}",
                    event.getSource(),
                    event.getPropertyName(),
                    event.getOldValue(),
                    event.getNewValue());
        });

        // 修改name属性来触发属性变更事件
        // null -> "xingguo"
        person.setName("xinguo");
        // "xingguo" -> "guoxing"
        person.setName("guoxing");
        person.setName("123456"); // 按照 勉强属性原则,当前属性不为设置成功,并会抛出异常
        log.info("person.name:{}", person.getName());
    }
}
