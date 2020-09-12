/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.first;

/**
 * CloneAbleDemo
 *
 * @author guoxing
 * @date 9/11/20 3:26 PM
 * @since
 */
public class CloneAbleDemo {

    public static void main(String[] args) throws CloneNotSupportedException {
        CloneAbleImpl cloneAble = new CloneAbleImpl(1);
        cloneAble.setS("1");
        // 如果当前类未实现CloneAble接口,则会直接抛出CloneNotSupportedException异常信息
        CloneAbleImpl clone = cloneAble.clone();
        System.out.println(clone.getValue());
        //结果为false,深拷贝
        System.out.println(cloneAble == clone);
        // 当为浅拷贝时,结果为true
        // 数据流程为 cloneAble对象的s字段 -> clone对象的s字段; 对于String类型的复制实际默认是浅拷贝
        System.out.println(cloneAble.getS() == clone.getS());
        // 当为深拷贝时,上述结果为false,而当前结果为true
        System.out.println(cloneAble.getS().equals(clone.getS()));
    }
}


class CloneAbleImpl implements Cloneable {
    private final int value;
    private String s;

    CloneAbleImpl(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public String getS() {
        return this.s;
    }

    public void setS(String s) {
        this.s = s;
    }


    /**
     * 重写object中的clone方法,并修改object中的clone的返回值和访问性
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public CloneAbleImpl clone() throws CloneNotSupportedException {
        CloneAbleImpl cloneAble = (CloneAbleImpl) super.clone();
        // 深拷贝,创建了一个新的String对象,对于s而言此时就是深拷贝
        cloneAble.setS(new String(this.s));
        return cloneAble;
    }
}