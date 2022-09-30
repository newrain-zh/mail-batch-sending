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

    /**
     * 重试失败类型
     * TODO 1.邮箱失败的原因有很多 细化根据失败的原因重试可以重发的
     * TODO 2.失败类型会占用邮件资源
     * TODO 3.优化-分布式锁
     *
     * @param date 日期
     */
    void startByError(String date);
}
