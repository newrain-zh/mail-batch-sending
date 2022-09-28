package com.example.service;

import com.example.entity.dto.BatchMailConfigDto;
import com.example.entity.pojo.MailSendLog;

import java.util.List;
import java.util.Map;

/**
 * @author zhiqin.zhang
 */
public interface IMailResourceService {

    /**
     * 检查邮箱资源是否已满
     */
    void checkMailResource();


    /**
     * 获取可用邮箱资源
     *
     * @return 邮件配置列表
     */
    List<BatchMailConfigDto> getMailResourceList();


    /**
     * 获取当前邮件资源的情况
     * 分析当前发送记录、Redis中的邮件资源使用情况
     *
     * @return 邮件发送详细结果
     */
    Map<String, Integer> getMailResourceInfo();

    /**
     * 获取15分钟内邮箱使用情况
     *
     * @param minute
     * @return
     */
    List<MailSendLog> getMailUsedRecord(int minute);
}
