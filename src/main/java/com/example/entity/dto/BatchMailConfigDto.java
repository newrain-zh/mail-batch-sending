package com.example.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * batch_mail_config
 * 邮件配置类
 *
 * @author zhiqin.zhang
 */
@Data
public class BatchMailConfigDto implements Serializable {
    /**
     * 邮件配置ID
     */
    private Long id;

    /**
     * 发件箱服务器地址
     */
    private String host;

    /**
     * 邮箱帐号
     */
    private String username;

    /**
     * 密码或授权码
     */
    private String password;

    /**
     * 发件人
     */
    private String fromName;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 配置备注
     */
    private String remark;

    /**
     * 帐号状态（1正常 0停用）
     */
    private Integer status;

    /**
     * 是否为SSL（0代表否，1代表是）
     */
    private Integer isSsl;

    /**
     * 删除标志（1代表存在 0代表删除）
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private Integer canUsedCount;

    private static final long serialVersionUID = 1L;
}