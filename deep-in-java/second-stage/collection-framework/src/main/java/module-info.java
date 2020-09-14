/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
module collection.framework { //定义当前模块的名称类似于maven的artifactId
    requires java.base; // 默认依赖, requires 后面加的是模块的模块名而非包名
    requires java.sql; // 在java.sql中的requires transitive 表示了依赖传递, 当引入 java.sql依赖后,会默认将java.sql中使用 requires transitive 的相关依赖也传递进来
    exports com.xingguo.modules; // 要暴露的当前模块的包下的类,这里要求导出包下必须存在java文件
}