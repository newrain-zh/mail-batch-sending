package com.example.enums;

/**
 * 邮件发送状态枚举
 *
 * @author zhiqin.zhang
 */
public enum MailSendStatusEnum {

    /**
     * 0=发送中
     * 1=成功
     * 2=失败
     * 3=保留状态
     */
    SENDING(0), SUCCESS(1), FAIL(2), NO_SEND(3);

    private final int code;


    public int getCode() {
        return code;
    }

    MailSendStatusEnum(int code) {
        this.code = code;
    }
}
