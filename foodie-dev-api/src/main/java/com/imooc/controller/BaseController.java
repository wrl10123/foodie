package com.imooc.controller;

import com.imooc.pojo.Orders;
import com.imooc.pojo.Users;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.IMOOCJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {

    public static final String FOODIE_SHOPCART = "shopcart";

    public static final Integer COMMENT_PAGE_SIZE = 10;
    public static final Integer SEARCH_PAGE_SIZE = 20;

    //支付中心的调用地址
    public static final String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    //微信支付成功 -> 支付中心 -> 天天吃货
    //回调通知的url（需要外网能够访问得到..............）
    public static final String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";

    //用户上传头像的位置
    public static final String IMAGE_USER_FACE_LOCATION = "d:"
            + File.separator + "v_irlwang"
            + File.separator + "images";

    @Autowired
    public MyOrdersService myOrdersService;

    /**
     * 校验订单是否存在
     * @param userId
     * @param orderId
     * @return
     */
    public IMOOCJSONResult checkUserOrder(String userId, String orderId) {
        Orders orders = myOrdersService.queryMyOrder(userId, orderId);
        if (orders == null) {
            return IMOOCJSONResult.errorMsg("订单不存在");
        }
        return IMOOCJSONResult.ok(orders);
    }

    /**
     * 用户属性设空
     *
     * @param users
     */
    public void setNullPreperty(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
    }

    /**
     * @param result
     * @return
     */
    public Map<String, String> getErrors(BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            //发送验证错误对应的某一个属性
            String errorField = error.getField();
            //验证错误的信息
            String defaultMessage = error.getDefaultMessage();
            errorMap.put(errorField, defaultMessage);
        }
        return errorMap;
    }

}
