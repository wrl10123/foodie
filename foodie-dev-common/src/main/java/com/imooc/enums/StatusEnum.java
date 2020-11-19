package com.imooc.enums;

/**
 * 性别枚举
 */
public enum StatusEnum {
    NO(0, "否"),
    YES(1, "是"),
    ;
    public Integer type;
    public String value;

    StatusEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
