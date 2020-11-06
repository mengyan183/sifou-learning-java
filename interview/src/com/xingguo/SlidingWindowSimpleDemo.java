/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SlidingWindowSimpleDemo
 * 滑动窗口
 * //TODO 尚未完全实现
 *
 * @author guoxing
 * @date 2020/11/6 3:18 PM
 * @since
 */
public class SlidingWindowSimpleDemo {
    public static void main(String[] args) {
        Date current = new Date();
        long time = current.getTime();
        SlidingWindowSimpleDemo slidingWindowSimpleDemo = new SlidingWindowSimpleDemo();
        AtomicInteger atomicInteger = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            Random random = new Random();
            while (true) {
                slidingWindowSimpleDemo.saveCount(new Date());
                int i = atomicInteger.incrementAndGet();
                System.out.println(i);
                try {
                    Thread.sleep(random.nextInt(1000 * 60));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.execute(() -> {
            while (true) {
                if ((new Date().getTime() - time) % (1000 * 30) == 0) {
                    System.out.println("当前时间 :" + new Date() + ";30秒内登陆次数为:" + slidingWindowSimpleDemo.total);
                }
            }
        });
        executorService.shutdown();
    }

    int[] countArray = new int[30];
    int total;
    // 从第一次进入时间作为计算周期的起始时间
    boolean isFirstCycle = true;
    Date startTime;
    int firstCycleLeftTime;
    int lastIndex = -1;

    public void saveCount(Date date) {
        if (startTime == null) {
            synchronized (this) {
                if (startTime == null) {
                    startTime = date;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    // 获取当前时间秒
                    int second = calendar.get(Calendar.SECOND);
                    // 这里使用30秒作为一个完整的周期
                    firstCycleLeftTime = second < 30 ? 30 - second : 59 - second + 1;
                }
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 获取当前时间秒
        int minutes = calendar.get(Calendar.SECOND);
        int index = minutes % countArray.length;
        // 判断是否完成了一个周期
        if (isFirstCycle) {
            // 毫秒转换为秒
            long betweenMinute = (date.getTime() - startTime.getTime()) / 1000;
            // 由于当前时间跨度超过了周期起始时间内的限定跨度,因此判定为当前已进入下一个周期
            if (betweenMinute > firstCycleLeftTime) {
                isFirstCycle = false;
                System.out.println("周期开始时间:" + startTime + ";进入下一个周期");
            }
        }
        /**
         * TODO: 当重复使用相同的数组时 存在历史数据清除的问题
         */
        if (!isFirstCycle) {
            //TODO : 由于当前的区块跨度为1秒,如果每秒内存在多次请求,唯一的解决方法就是对每个区块的跨度再次缩小
            int pre = 0;
            // 需要清除中间区域的数据
            for (int i = lastIndex == -1 ? index : lastIndex; i <= index; i++) {
                pre += countArray[i];
                countArray[i] = 0;
            }
            total -= pre;
        }
        countArray[index] += 1;
        total += 1;
        lastIndex = index;
    }
}
