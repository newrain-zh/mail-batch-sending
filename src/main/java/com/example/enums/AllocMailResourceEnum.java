package com.example.enums;

/**
 * 资源分配类型枚举
 *
 * @author zhiqin.zhang
 */

public enum AllocMailResourceEnum {
    /**
     * default = 轮流分配
     * random  = 随机分配
     */
    DEFAULT("default"), RANDOM("random"),
    ;

    private String type;

    AllocMailResourceEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
