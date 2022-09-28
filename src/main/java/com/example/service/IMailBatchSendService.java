package com.example.service;

/**
 * 批量发送邮件
 *
 * @author zhiqin.zhang
 */
public interface IMailBatchSendService {

    /**
     * 开始发送邮件job
     *
     * @param date 日期
     */
    void start(String date);
}
