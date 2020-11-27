package com.imooc.controller;

import com.imooc.enums.PayMethodEnum;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.service.OrderService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Api(value = "订单相关接口")
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "创建订单", httpMethod = "POST")
    @PostMapping("create")
    public IMOOCJSONResult list(@RequestBody SubmitOrderBO submitOrderBO,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        log.info("{}", submitOrderBO);

        if (!submitOrderBO.getPayMethod().equals(PayMethodEnum.WEIXIN.type)
                && !submitOrderBO.getPayMethod().equals(PayMethodEnum.ALIPAY.type)) {
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }
        //1. 创建订单
        String orderId = orderService.createOrder(submitOrderBO);

        //2. 移除购物车中已结算的商品
        //todo 整合redis之后，移除购物车中已结算的商品
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);

        //3. 向支付中心发送当前订单，用于保存支付中心订单数据

        return IMOOCJSONResult.ok(orderId);
    }

    @PostMapping("notifyMerchantOrderPaid")
    public IMOOCJSONResult notifyMerchantOrderPaid(String merchantOrderId){


        return null;
    }

}
