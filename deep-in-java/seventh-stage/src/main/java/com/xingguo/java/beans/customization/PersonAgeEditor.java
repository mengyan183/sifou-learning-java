/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.customization;

import com.xingguo.java.beans.properties.Person;
import org.springframework.beans.PropertyValuesEditor;

import java.beans.PropertyEditorSupport;

/**
 * PersonAgeEditor
 * person bean age 属性修改
 *
 * @author guoxing
 * @date 2020/11/24 7:14 PM
 * @see PropertyValuesEditor
 * @since
 */
public class PersonAgeEditor extends PropertyEditorSupport {
    /**
     * 类似于 spring xml bean 属性定义, 对于xml解析而言,所有定义的属性信息都是字符串类型,而对于实际的beanProperty 可能是非String类型数据,因此对于这种解析操作,需要利用 PropertyEditor 来支持 customization
     *
     * @param text
     * @throws IllegalArgumentException
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (Person.isNumeric(text)) {
            // 对于当前操作实际存在 publish event操作
            // 但对于其PropertyEvent 实际只传递了 操作对象实例,并未传递值
            setValue(Long.valueOf(text));
        }
    }

    @Override
    public Integer getValue() {
        return Integer.parseInt(super.getValue().toString());
    }
}
