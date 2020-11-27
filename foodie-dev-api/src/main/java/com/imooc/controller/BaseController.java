package com.imooc.controller;

public class BaseController {

    public static final String FOODIE_SHOPCART = "shopcart";

    public static final Integer COMMENT_PAGE_SIZE = 10;
    public static final Integer SEARCH_PAGE_SIZE = 20;

    //支付中心的调用地址
    public static final String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    //微信支付成功 -> 支付中心 -> 天天吃货
    //回调通知的url（需要外网能够访问得到..............）
    public static final String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";

}
