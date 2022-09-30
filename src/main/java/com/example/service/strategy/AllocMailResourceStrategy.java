package com.example.service.strategy;

import com.example.entity.dto.BatchMailConfigDto;
import com.example.entity.pojo.MailSendLog;

import java.util.List;

/**
 * 邮箱分配资源策略
 * 一般邮箱服务商提供的邮箱都会有发送限制
 * 这里用于控制分配邮件资源
 *
 * @author zhiqin.zhang
 */
public interface AllocMailResourceStrategy {

    /**
     * 分配资源
     *
     * @param mailResourceList 资源列表
     * @param list             待分配账号
     */
    void allocMailResource(List<BatchMailConfigDto> mailResourceList, List<MailSendLog> list);

    /**
     * 策略类型和文件类型直接关联
     *
     * @return 策略名称
     */
    String getType();


}
