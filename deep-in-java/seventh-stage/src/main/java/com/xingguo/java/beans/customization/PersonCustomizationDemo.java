/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.customization;

import com.xingguo.java.beans.properties.Person;
import lombok.extern.slf4j.Slf4j;

import java.beans.*;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * PersonCustomizationDemo
 *
 * @author guoxing
 * @date 2020/11/24 9:26 PM
 * @since
 */
@Slf4j
public class PersonCustomizationDemo {

    public static void main(String[] args) throws IntrospectionException {
        // 模拟 <property name="age">18</property>
        Person person = new Person();

        // 通过 内省来获取到所有的property
        BeanInfo beanInfo = Introspector.getBeanInfo(Person.class, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        if (Objects.nonNull(propertyDescriptors)) {
            // 对于propertyDescriptor 中的name属性实际就等价于baseName字段
            Stream.of(propertyDescriptors)
                    .filter(propertyDescriptor -> "age".equals(propertyDescriptor.getName()))
                    .findFirst()
                    .ifPresent(propertyDescriptor -> {
                        // 对当前属性设置 自定义 propertyEditor
                        propertyDescriptor.setPropertyEditorClass(PersonAgeEditor.class);
                        PropertyEditor propertyEditor = propertyDescriptor.createPropertyEditor(PersonAgeEditor.class);
                        //对于 propertyEditor 需要增加相应的事件监听器来获取到 setValue/setAsText传递的数据
                        // 对于 propertySupport 中当 set操作完成后会执行 java.beans.PropertyEditorSupport.firePropertyChange 发送事件
                        // 因此需要得到相关的事件信息
                        propertyEditor.addPropertyChangeListener(event -> {
                            // 对于当前操作而言, 因为 对于 propertyEditor 实际只是将外部传递的数据做自定义数据处理并存储,并不支持直接将转换后的数据赋值给要操作的Property所属对象;对于propertyEditor 并没有属于某个对象的含义,其存在的含义是工作类(插件)的作用
                            // 因此对于 真正的对象属性赋值仍然需要手动赋值操作; 因此需要通过监听 propertyEditor#value 属性变更事件来获取到传递的数据("18")
                            person.setAge((int) propertyEditor.getValue());
                        });
                        propertyEditor.setAsText("18");
                    });
            log.info("person.age:{}", person.getAge());
        }
        // TODO : java.beans.PropertyEditorManager 的用途
        
    }
}
