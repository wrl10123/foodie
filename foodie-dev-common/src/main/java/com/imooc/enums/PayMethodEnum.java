package com.imooc.enums;

/**
 * 支付方式枚举
 */
public enum PayMethodEnum {
    WEIXIN(1, "微信"),
    ALIPAY(2, "支付宝"),
    ;
    public Integer type;
    public String value;

    PayMethodEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
