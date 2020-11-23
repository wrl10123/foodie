package com.imooc.controller;

import com.imooc.pojo.bo.UserAddressBO;
import com.imooc.service.AddressService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "地址相关接口")
@RestController
@RequestMapping("address")
public class AddressController {

    /**
     * 1.查询用户的所有收获地址列表
     * 2.新增
     * 3.删除
     * 4.修改
     * 5.设置默认地址
     */

    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "查询收获地址")
    @PostMapping("list")
    public IMOOCJSONResult list(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        List list = addressService.queryAll(userId);
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "添加收获地址")
    @PostMapping("add")
    public IMOOCJSONResult add(@RequestBody UserAddressBO userAddressBO) {
        IMOOCJSONResult result = checkAddressBo(userAddressBO);
        if (result.getStatus() != 200) {
            return result;
        }
        addressService.addUserAddress(userAddressBO);
        return IMOOCJSONResult.ok();
    }

    private IMOOCJSONResult checkAddressBo(UserAddressBO userAddressBO) {
        if (userAddressBO.getReceiver().length() > 16) {
            return IMOOCJSONResult.ok("收货人姓名不能太长");
        }
        if (userAddressBO.getMobile().length() != 11
                || MobileEmailUtils.checkEmailIsOk(userAddressBO.getMobile())) {
            return IMOOCJSONResult.ok("手机号格式不正确");
        }
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "修改收获地址")
    @PostMapping("update")
    public IMOOCJSONResult update(@RequestBody UserAddressBO userAddressBO) {
        IMOOCJSONResult result = checkAddressBo(userAddressBO);
        if (StringUtils.isBlank(userAddressBO.getAddressId())) {
            return IMOOCJSONResult.errorMsg("地址id不可为空");
        }
        if (result.getStatus() != 200) {
            return result;
        }
        addressService.updateUserAddress(userAddressBO);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "删除收获地址")
    @PostMapping("delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @RequestParam String addressId) {
        if (StringUtils.isBlank(userId)
                || StringUtils.isBlank(addressId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        addressService.delete(userId, addressId);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "设置默认地址")
    @PostMapping("setDefalut")
    public IMOOCJSONResult setDefalut(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @RequestParam String addressId) {
        if (StringUtils.isBlank(userId)
                || StringUtils.isBlank(addressId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        addressService.setDefalut(userId, addressId);
        return IMOOCJSONResult.ok();
    }

}
