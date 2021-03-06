package com.imooc.enums;

/**
 * 性别枚举
 */
public enum CommentLevelEnum {
    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3, "差评"),
    ;
    public Integer type;
    public String value;

    CommentLevelEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
