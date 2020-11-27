package com.xingguo.reflection.application;

import java.io.Serializable;

/**
 * 实际执行
 */
@Repository(tableName = "user")
public class UserRepositoryImpl implements UserRepository<User>,// 对于当前接口的类型实际属于 ParameterizedType 继承至Type ,对于java.lang.reflect.ParameterizedType.getActualTypeArguments 实际就是 UserRepository<User> 中的 "<>"中的User类型
        Comparable<UserRepositoryImpl>,//和UserRepository<User>类型一样都是ParameterizedType
        Serializable // 对于当前类型而言,其为 Class类型
{
    @Override
    public int compareTo(UserRepositoryImpl o) {
        return 0;
    }
}
