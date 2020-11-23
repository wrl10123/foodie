package com.imooc.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("用户收获地址")
@Data
public class UserAddressBO {

    @ApiModelProperty(value = "地址id")
    private String addressId;

    @ApiModelProperty(value = "用户id", required = true)
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @NotBlank(message = "收货人不能为空")
    private String receiver;
    @NotBlank(message = "收货人手机号不能为空")
    private String mobile;
    @NotBlank(message = "省份不能为空")
    private String province;
    @NotBlank(message = "城市不能为空")
    private String city;
    @NotBlank(message = "地区不能为空")
    private String district;
    @NotBlank(message = "详细地址不能为空")
    private String detail;
}
