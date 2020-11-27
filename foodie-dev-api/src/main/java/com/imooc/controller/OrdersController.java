package com.imooc.controller;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayMethodEnum;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrderVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.OrderService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Api(value = "订单相关接口")
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

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
        OrderVO order = orderService.createOrder(submitOrderBO);
        String orderId = order.getOrderId();

        //2. 移除购物车中已结算的商品
        //todo 整合redis之后，移除购物车中已结算的商品
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);

        //3. 向支付中心发送当前订单，用于保存支付中心订单数据
        MerchantOrderVO merchantOrderVO = order.getMerchantOrderVO();
        merchantOrderVO.setReturnUrl(payReturnUrl);

        //1分钱，方便测试
        merchantOrderVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "imooc");
        headers.add("password", "imooc");
        HttpEntity<MerchantOrderVO> httpEntity = new HttpEntity<>(merchantOrderVO, headers);
        ResponseEntity<IMOOCJSONResult> responseEntity =
                restTemplate.postForEntity(paymentUrl, httpEntity, IMOOCJSONResult.class);
        IMOOCJSONResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            return IMOOCJSONResult.errorMsg("支付中心订单创建失败，联系管理员");
        }
        return IMOOCJSONResult.ok(orderId);
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo(String orderId) {
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return IMOOCJSONResult.ok(orderStatus);
    }
}
