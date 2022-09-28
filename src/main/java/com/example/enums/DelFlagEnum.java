package com.example.enums;

/**
 * 删除状态
 *
 * @author zhiqin.zhang
 */
public enum DelFlagEnum {
    /**
     * 未删除
     */
    YES(1),
    /**
     * 已删除
     */
    NO(0);

    private final int value;

    DelFlagEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
