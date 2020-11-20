package com.imooc.controller;

import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 购物车
 */
@Slf4j
@Api(value = "购物车", tags = {"购物车接口相关的api"})
@RestController
@RequestMapping("shopcart")
public class ShopCartController {

    @ApiOperation(value = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("add")
    public IMOOCJSONResult carousel(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        log.info("{}", shopcartBO);

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        //todo 前端用户在登录的情况下添加商品到购物差，会同步到redis

        return IMOOCJSONResult.ok();
    }


}
