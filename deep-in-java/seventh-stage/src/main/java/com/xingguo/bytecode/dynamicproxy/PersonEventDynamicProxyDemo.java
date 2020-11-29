/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.bytecode.dynamicproxy;

import com.xingguo.bytecode.beans.ProxyPojo;
import lombok.extern.slf4j.Slf4j;

import java.beans.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

import static com.xingguo.java.beans.properties.Person.isNumeric;

/**
 * PersonEventDynamicProxyDemo
 * 对于 {@link NameableProxyService} 的动态代理提升
 *
 * @author guoxing
 * @date 2020/11/28 4:22 PM
 * @since
 */
@Slf4j
public class PersonEventDynamicProxyDemo {
    public static void main(String[] args) {

        /**
         * 对于 不使用动态代理的操作如下
         */
        ProxyPojo proxyPojo = new ProxyPojo();
        NameablePropertyEventInvocationHandler nameablePropertyEventInvocationHandler = new NameablePropertyEventInvocationHandler(proxyPojo);
        // 增加 PropertyChange listener
        nameablePropertyEventInvocationHandler.addPropertyChangeListener(event -> {
            log.info("强迫属性事件");
            log.info("source:{}中的属性:{}发生变化,oldVal为{},newVal为{}",
                    event.getSource(),
                    event.getPropertyName(),
                    event.getOldValue(),
                    event.getNewValue());
        });
        // 增加 VetoableChange listener
        nameablePropertyEventInvocationHandler.addVetoableChangeListener(event -> {
            log.info("勉强属性事件");
            log.info("source:{}中的属性:{}发生变化,oldVal为{},newVal为{}",
                    event.getSource(),
                    event.getPropertyName(),
                    event.getOldValue(),
                    event.getNewValue());
        });

        // 获取当前线程上下文的 类加载器
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        // 创建动态代理类
        // 对于 动态代理的创建实际支持传递多个接口, 但也可以指定任意一个接口进行强制转换
        /**
         *  对于JDK动态代理的缺点也在于其基于接口模式,当一个实现类实现多个接口时,就会需要编写过多的重复代码去增强多个接口
         */
        NameableProxyService nameableProxyService = (NameableProxyService) Proxy.newProxyInstance(contextClassLoader, new Class[]{NameableProxyService.class}, nameablePropertyEventInvocationHandler);
        /**
         * 对于 Proxy.newInstance 得到的结果进行分析
         * 类型:.getClass -> com.sun.proxy.$Proxy0
         * superclass -> java.lang.reflect.Proxy
         * interfaces -> Proxy.newProxyInstance# interfaces 参数
         * 对于 JDK 动态代理 得到的代理类实际并没有具体的类型定义,动态代理和普通类 的区别就在于动态代理是属于运行时动态生成的 类(实例), 生成的结果实际是通过组装而成的,首先是继承了 java.lang.reflect.Proxy类,其次是 实现的传递的 interfaces 参数中的全部接口
         *
         * 其缺陷: 对于JDK动态代理而言,其实现主要是对接口的增强,因此在使用动态代理类时必须要指定单独的接口,当实现多个接口时,就需要对代理类进行多次强制类型转换为指定的接口类型
         */
        nameableProxyService.setName("guoxing");
        nameableProxyService.setName("xingguo");
        nameableProxyService.setName("1234");

    }
}

/**
 * 对于 {@link com.xingguo.bytecode.beans.ProxyPojo#setName(String)} 的 {@link java.beans.PropertyChangeEvent} 的动态代理实现
 */
class NameablePropertyEventInvocationHandler implements InvocationHandler {
    private ProxyPojo proxyPojo;

    // 强迫属性工具类
    private transient final PropertyChangeSupport propertyChangeSupport;
    // 勉强属性工具类
    private transient final VetoableChangeSupport vetoableChangeSupport;

    public NameablePropertyEventInvocationHandler(ProxyPojo proxyPojo) {
        this.proxyPojo = proxyPojo;
        propertyChangeSupport = new PropertyChangeSupport(proxyPojo);
        vetoableChangeSupport = new VetoableChangeSupport(proxyPojo);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        // 对于 void setName(String) 判断
        if ("setName".equals(name) // 判断方法名称是否为 "setName"
                && void.class.equals(method.getReturnType()) // 判断返回值类型是否为 void
                && Objects.nonNull(args) // 判断 参数是否为空
                && args.length == 1 // 判断长度是否为1
                && args[0] instanceof String // 是否为 String 类型
        ) {
            String oldVal = proxyPojo.getName();
            String newVal = (String) args[0];
            // 创建属性变更事件
            PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this, "name", oldVal, newVal);
            // 勉强属性
            fireVetoableChange(propertyChangeEvent);
            proxyPojo.setName(newVal);
            // 强迫事件变更机制
            // 强迫事件变更发生在 属性成功变更之后
            // 属性变更后发布事件
            // 发布事件
            firePropertyChange(propertyChangeEvent);
        }
        return null;
    }

    /**
     * 强迫属性相关
     *
     * @param event
     */
    public void firePropertyChange(PropertyChangeEvent event) {
        propertyChangeSupport.firePropertyChange(event);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }


    /**
     * 勉强属性相关
     *
     * @param event
     */
    public void fireVetoableChange(PropertyChangeEvent event) {
        try {
            // 校验要求name 不能为纯粹的数字
            String propertyName = event.getPropertyName();
            if ("name".equals(propertyName) && isNumeric(String.valueOf(event.getNewValue()))) {
                throw new PropertyVetoException("name属性要求不能为纯数字", event);
            }
            vetoableChangeSupport.fireVetoableChange(event);
        } catch (PropertyVetoException propertyVetoException) {
            throw new RuntimeException(propertyVetoException);
        }

    }

    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }
}
