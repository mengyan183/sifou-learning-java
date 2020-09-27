/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
module concurrent.basic {
    requires java.base;
    requires java.management;
    requires java.logging;
    requires org.mapstruct.processor;
    requires org.slf4j;
    requires org.slf4j.simple;
    requires static lombok;
    requires jdk.unsupported;
    exports com.xingguo.concurrent.basic;
}