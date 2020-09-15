import com.xingguo.modules.service.FooService;
import com.xingguo.modules.service.impl.DriverImpl;
import com.xingguo.modules.service.impl.FooServiceImpl;

/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
//module java.base{// 当存在模板路径名和依赖路径名重名时会出现依赖冲突,提示循环依赖
module collection.framework { //定义当前模块的名称类似于maven的artifactId
    requires java.base; // 默认依赖, requires 后面加的是模块的模块名而非包名
    requires java.sql; // 在java.sql中的requires transitive 表示了依赖传递, 当引入 java.sql依赖后,会默认将java.sql中使用 requires transitive 的相关依赖也传递进来
    // 引入第三方未命名的
    requires spring.context;
    requires org.apache.commons.lang3; // 对于存在 Automatic-Module-Name 的maven 依赖 可以直接使用 META-INF下MANIFEST.MF中的Automatic-Module-Name 对应的数据进行依赖
    requires commons.collections; // 对于不存在Automatic-Module-Name 的maven依赖,只需要通过将 maven 的artifactId 中的 "-" 转换"." 使用requires 进行依赖

    exports com.xingguo.modules /*to java.sql*/; // 要暴露的当前模块的包下的类,这里要求导出包下必须存在java文件; 使用to 表示将当前包导出指向给指定模块使用
    uses FooService; // 指定当前类可以作为引入模块的服务消费者,作为当前模块的服务提供者
    provides FooService with FooServiceImpl;// 指定当前服务的实现类
    provides java.sql.Driver with DriverImpl;
}