/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * AnimalSPIDemo
 * {@link ServiceLoader}
 * {@link Animal src/META-INF/services/com.xingguo.spi.Animal}
 *
 * @author guoxing
 * @date 2020/12/20 12:20 PM
 * @since
 */
public class AnimalSPIDemo {
    public static void main(String[] args) {
        ServiceLoader<Animal> animals = ServiceLoader.load(Animal.class, Thread.currentThread().getContextClassLoader());
        Iterator<Animal> iterator = animals.iterator();
        while (iterator.hasNext()) {
            Animal next = iterator.next();
            next.eat();
        }
    }
}
