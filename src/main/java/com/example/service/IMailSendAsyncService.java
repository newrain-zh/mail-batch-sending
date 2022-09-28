package com.example.service;

import com.example.entity.dto.BatchMailConfigDto;
import com.example.entity.pojo.MailSendLog;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhiqin.zhang
 */
public interface IMailSendAsyncService {

    /**
     * 异步执行任务
     *
     * @param collect        待发送邮件
     * @param map            邮件配置
     * @param countDownLatch 计数器
     */
    void executeAsync(List<MailSendLog> collect, Map<String, BatchMailConfigDto> map, CountDownLatch countDownLatch);
}
