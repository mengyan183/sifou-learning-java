/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.reflection;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassMembers
 *
 * @author guoxing
 * @date 2020/11/10 4:32 PM
 * @since
 */
@Slf4j
public class ClassMembers {
    // 类字段
    /**
     * 对于以下两个字段的唯一区别在于 使用使用了 final 字段修饰
     * 由于都是使用的static 修饰表示可以直接通过了类名来直接访问当前字段,因此当前字段属于类字段,对于类字段而言相当于在当前Class编译加载阶段当前字段的数据会被加载,对于当前ClassLoader而言,属于全局唯一的;
     * 当增加了final修饰符之后,对于编译器而言,由于其不可变性,当前字段就属于一个常量不可变数据,在编译阶段会有一定的提升,例如使用常量字段做一些编译期运算,而非运行时计算,因此对于可以编译期运算的数据,都是属于常量数据
     */
    public static final Integer I = 0;
//    public static Integer I = 0;
    // 对象字段/属性
    /**
     * 对于当前属性由于其可变性且生命周期存在于对象实例阶段,因此它是属于每个对象实例所有
     */
//    private String name;
    /**
     * 如果对象字段使用final 关键字,则只有两种处理方式
     * 1:在声明当前字段时指定当前字段的值且不允许修改,在某种意义上实际类似于常量字段
     * 2:对于当前字段肯定存在对应的有参构造
     */
//    private final String name = "1";
    private final String name;
    private Integer age;

    /**
     * 构造器方法:
     * 所属
     * 修饰符:只能使用 public/protected/default/private 等修饰符
     * 构造器名称:只能是所属类的类名; 个人理解,对于当前构造器名称实际既代表了方法名也代表了返回类型
     * 构造器参数: 可以是无参构造也可以有多个重载的有参构造
     * 方法异常列表
     *
     * @param name
     */
    public ClassMembers(String name) throws RuntimeException{
        this.name = name;
    }
    public String getName(){
        return this.name;
    }


    /**
     * 方法
     * 所属 :当前main方法属于当前ClassMembers类
     * 方法修饰符 : public static
     * 返回类型 : void
     * 方法名 : main
     * 方法参数 : 字符串数组
     * 异常列表 : 无
     *
     * @param args
     */
    public static void main(String[] args) {
        // 对于类字段和普通属性的区别在于 类字段属于 Class对象所有,并不会跟随着类实例变化
        ClassMembers classMembers = new ClassMembers("xingguo");
        String name = classMembers.name;
//        ClassMembers.I = 1;
        log.info("{}",ClassMembers.I);
    }
}

class CustomClassMembers extends ClassMembers{
    public static final Integer I = 0;

    private String name;
    //Variable 'name' is already defined in the scope
//    private String name;

    public CustomClassMembers(String name) {
        super(name);
    }
    @Override
    public String getName(){
        return this.name;
    }

    public static void main(String[] args) {

    }
}
