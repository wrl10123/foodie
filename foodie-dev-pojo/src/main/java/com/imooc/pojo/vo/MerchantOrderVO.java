package com.imooc.pojo.vo;

import lombok.Data;

@Data
public class MerchantOrderVO {

    private String merchantOrderId;     //商户订单号
    private String merchantUserId;      //商户方的发起用户的的主键id
    private Integer amount;             //实际支付总金额
    private Integer payMethod;          //支付方式
    private String returnUrl;           //支付成功后的回调地址

}
