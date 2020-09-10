/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
module basic { // 该名称的含义为代表当前module的名称
//    requires java.base;//默认已引入
    requires java.desktop;
//    requires java.logging;
    requires java.sql; // 由于在java.sql模块中设置了 requires transitive java.logging; 对于transitive关键字的作用为对于引入当前的依赖可以直接使用引入依赖中声明transitive的依赖
    uses java.sql.Driver;// 可以直接使用类
    exports com.xingguo.function.base;// 将当前工程下的指定包下的全部的class都暴露
    exports com;
}