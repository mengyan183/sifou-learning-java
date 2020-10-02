/*
 * Copyright (c) 2020, guoxing, Co,. Ltd. All Rights Reserved
 */
package com.xingguo.concurrent.frame.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

/**
 * FlowApiDemo
 *
 * @author guoxing
 * @date 2020/10/2 12:00 PM
 * @since
 */
@Slf4j
public class FlowApiDemo {
    public static void main(String[] args) {
        try (SubmissionPublisher<String> submissionPublisher = new SubmissionPublisher<>()) {
            submissionPublisher.subscribe(new Flow.Subscriber<String>() {
                private Flow.Subscription subscription;

                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    log.info("已订阅");
                    // 对订阅进行处理
                    // 限制订阅者可以接收的数据量上限
                    subscription.request(Long.MAX_VALUE);
                    this.subscription = subscription;
                }

                @Override
                public void onNext(String item) {
                    //对接收到的数据进行处理
                    if ("exit".equalsIgnoreCase(item)) {
                        // 取消订阅
                        subscription.cancel();
                        log.info("取消订阅");
                        return;
                    } else if ("exception".equalsIgnoreCase(item)) {
                        throw new RuntimeException("发生异常");
                    }
                    log.info("处理数据:{}", item);
                }

                @Override
                public void onError(Throwable throwable) {
                    log.error("处理数据发生异常", throwable);
                }

                @Override
                public void onComplete() {
                    log.info("完成全部提交的数据");
                }
            });
            submissionPublisher.submit("发布事件");
            int exitCode = new Random().nextInt(20);
            int exceptionCode = new Random().nextInt(20);
            for (int i = 0; i < 20; i++) {
                if (i == exitCode) {
                    submissionPublisher.submit("exit");
                } else if (i == exceptionCode) {
                    submissionPublisher.submit("exception");
                } else {
                    submissionPublisher.submit(String.valueOf(i));
                }
            }
            ExecutorService executorService = (ExecutorService) submissionPublisher.getExecutor();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
