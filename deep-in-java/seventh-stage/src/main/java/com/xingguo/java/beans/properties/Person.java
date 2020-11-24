/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.properties;

import java.beans.*;

/**
 * Person
 *
 * @author guoxing
 * @date 2020/11/24 11:28 AM
 * @since
 */
public class Person {
    private String name;

    private int age;

    // 设置当前Bean的属性变化支持工具
    // 强制属性更新
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    // 勉强属性更新
    private final VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);

    public String getName() {
        return name;
    }


    /**
     * 当名称属性发生变化时,
     *
     * @param name
     */
    public void setName(String name) {
        String oldVal = this.name;
        String newVal = name;
        // 创建属性变更事件
        PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this, "name", oldVal, newVal);
        // 勉强属性
        fireVetoableChange(propertyChangeEvent);
        this.name = name;
        // 强迫事件变更机制
        // 强迫事件变更发生在 属性成功变更之后
        // 属性变更后发布事件
        // 发布事件
        propertyChangeSupport.firePropertyChange(propertyChangeEvent);
    }

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

    }

    public void removeVetoableChangeListener(VetoableChangeListener listener) {

    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }
}
