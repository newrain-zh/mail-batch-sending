package com.example.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhiqin.zhang
 */
@Component
@ConfigurationProperties(prefix = "mail.wy")
@Data
public class MailsConfig {

    /**
     * 每个邮箱每天最大次数
     */
    @Value(("${day.total:1000}"))
    private long mailMaxDayTotal;

    /**
     * 每个邮箱每15分钟最大发送次数
     */
    @Value(("${minute.total:500}"))
    private int mailMaxMinuteTotal;

    /**
     * 用于控制发送的时间
     */
    @Value("${minute.rate:15}")
    private int mailMinuteRate;

    /**
     * 发送邮件线程池核心数大小
     */
    @Value("${pool.size:5}")
    private int mailPoolSize;

    /**
     * 分配策略 默认轮流分配
     */
    @Value("${alloc.mail.strategy:default}")
    private String allocMailStrategy;

    /**
     * job执行周期
     * 影响邮件发送数量、批次速率、补偿策略执行的成功率
     */
    @Value("${mail.job.cron:0 0/5 * * * ?}")
    private String mailJobCron;
}
