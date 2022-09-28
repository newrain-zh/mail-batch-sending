package com.example.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * mail_send_log
 * 邮箱发送记录
 *
 * @author
 */
@Data
public class MailSendLogDto implements Serializable {
    /**
     * 邮件记录ID
     */
    private Long id;

    /**
     * 发件人邮箱帐号
     */
    private String username;

    /**
     * 收件人邮箱
     */
    private String toUser;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 发送状态（0失败 1成功）
     */
    private Integer sendStatus;

    /**
     * 发送失败原因
     */
    private String sendFailResult;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    private static final long serialVersionUID = 1L;
}