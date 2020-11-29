/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.bytecode.dynamicproxy.cglib;

import com.xingguo.bytecode.beans.ProxyPojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.beans.*;
import java.lang.reflect.Method;
import java.util.Objects;

import static com.xingguo.java.beans.properties.Person.isNumeric;

/**
 * CGLibProxyDemo
 * {@link MethodInterceptor} cglib提供的方法增强接口
 * {@link Enhancer} 通过cglib提供的当前工具类 来生成 代理对象
 * {@see org.springframework.context.annotation.ConfigurationClassPostProcessor#enhanceConfigurationClasses(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)}
 * {@see org.springframework.context.annotation.ConfigurationClassEnhancer#enhance(java.lang.Class, java.lang.ClassLoader)}
 *
 * @author guoxing
 * @date 2020/11/29 4:26 PM
 * @since
 */
@Slf4j
public class CGLibProxyDemo {

    public static void main(String[] args) {
        // 创建代理对象实例
        // 对于cglib 得到的动态代理对象类型肯定是继承于被代理对象,因此可以直接强制类型转换
        ProxyPojo proxyPojo = (ProxyPojo) createNewInstance(ProxyPojo.class);
        proxyPojo.setName("xingguo");
        log.info("{}", proxyPojo);
        proxyPojo.setName("guoxing");
        log.info("{}", proxyPojo);
        proxyPojo.setName("123456");
        log.info("{}", proxyPojo);
    }

    public static Object createNewInstance(Class<?> klass) {
        NameablePropertyEventCallBack nameablePropertyEventCallBack = new NameablePropertyEventCallBack();

        // 增加 PropertyChange listener
        nameablePropertyEventCallBack.addPropertyChangeListener(event -> {
            log.info("强迫属性事件");
            log.info("source:{}中的属性:{}发生变化,oldVal为{},newVal为{}",
                    event.getSource(),
                    event.getPropertyName(),
                    event.getOldValue(),
                    event.getNewValue());
        });
        // 增加 VetoableChange listener
        nameablePropertyEventCallBack.addVetoableChangeListener(event -> {
            log.info("勉强属性事件");
            log.info("source:{}中的属性:{}发生变化,oldVal为{},newVal为{}",
                    event.getSource(),
                    event.getPropertyName(),
                    event.getOldValue(),
                    event.getNewValue());
        });
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(klass);
        enhancer.setCallback(nameablePropertyEventCallBack);
        // 对于生成的 被代理对象的子类 实际也调用了 {@link java.lang.ClassLoader.defineClass(java.lang.String, byte[], int, int, java.security.ProtectionDomain)}; 创建了对应的class 对象,并调用代理对象的newInstance方法来创建新的对象
        return enhancer.create();
    }
}

/**
 * 基于 cglib 方法拦截器接口实现
 */
class NameablePropertyEventCallBack implements MethodInterceptor {

    // 强迫属性工具类
    private transient final PropertyChangeSupport propertyChangeSupport;
    // 勉强属性工具类
    private transient final VetoableChangeSupport vetoableChangeSupport;

    public NameablePropertyEventCallBack() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        vetoableChangeSupport = new VetoableChangeSupport(this);
    }

    /**
     * @param o           动态代理对象实例-> com.xingguo.bytecode.dynamicproxy.cglib.CGLibProxyDemo#createNewInstance(java.lang.Class)
     * @param method      方法类型
     * @param args        方法参数
     * @param methodProxy 方法代理
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        String name = method.getName();
        // 对于 void setName(String) 判断
        if ("setName".equals(name) // 判断方法名称是否为 "setName"
                && void.class.equals(method.getReturnType()) // 判断返回值类型是否为 void
                && Objects.nonNull(args) // 判断 参数是否为空
                && args.length == 1 // 判断长度是否为1
                && args[0] instanceof String // 是否为 String 类型
        ) {
            ProxyPojo proxyPojo = (ProxyPojo) o;
            String oldVal = proxyPojo.getName();
            String newVal = (String) args[0];
            // 创建属性变更事件
            PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this, "name", oldVal, newVal);
            // 勉强属性
            fireVetoableChange(propertyChangeEvent);
            // 调用当前代理对象实例的父类(super)方法实现相关操作,而非直接通过 代理对象(o)进行操作
            methodProxy.invokeSuper(o, args);
            // 强迫事件变更机制
            // 强迫事件变更发生在 属性成功变更之后
            // 属性变更后发布事件
            // 发布事件
            firePropertyChange(propertyChangeEvent);
            return null;
        } else {
            // 获取当前代理类对象 o 的父类方法
            return methodProxy.invokeSuper(o, args);
        }
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
