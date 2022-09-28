package com.example.enums;

/**
 * 删除状态
 *
 * @author zhiqin.zhang
 */
public enum TaskStatusEnum {
    /**
     * 0 = 新建
     * 1 = 运行
     * 2 = 失败
     */
    LOCK(0), RUNNING(1);

    private final int value;

    TaskStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
