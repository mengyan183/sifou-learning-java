/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.java.beans.persistence;

import com.xingguo.java.beans.properties.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.FileSystemNotFoundException;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * PersonPersistence
 *
 * @author guoxing
 * @date 2020/11/24 10:34 PM
 * @since
 */
@Slf4j
public class PersonPersistence {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Person person = new Person();
        person.setAge(18);
        person.setName("guoxing");
        // 获取当前编译路径
        // 需要获取到 启动命令中的 -classpath 中的 classes文件夹
        Properties properties = System.getProperties();
        String classpath = System.getProperty("java.class.path");
        String[] split = classpath.split(":");
        String currentCompilerClassPath = Stream.of(split)
                .filter(s -> StringUtils.endsWithIgnoreCase(s, "classes"))
                .map(File::new) // 转换为文件
                .filter(File::isDirectory)
                .filter(File::canRead)
                .filter(File::canWrite)
                .map(File::getAbsolutePath)
                .findFirst()
                .orElseThrow(FileSystemNotFoundException::new);
//        OutputStream outputStream = new FileOutputStream(new File(currentCompilerClassPath, "person.tmp"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(person);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        // 通过断点可以看出来 对于 未使用 "transient" 关键字的属性,通过流传递之后会得到相应的数据
        // 而对于使用了 "transient" 关键字的属性, 在转换之后就会变成其默认值
        Person newPerson = (Person) objectInputStream.readObject();
        log.info("{}", newPerson);
    }
}
