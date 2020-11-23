package com.imooc.service;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.UserAddressBO;

import java.util.List;

public interface AddressService {

    /**
     * 查询用户地址列表
     *
     * @param userId
     * @return
     */
    List<UserAddress> queryAll(String userId);

    /**
     * 添加地址
     *
     * @param userAddressBO
     */
    void addUserAddress(UserAddressBO userAddressBO);

    /**
     * 修改地址
     *
     * @param userAddressBO
     */
    void updateUserAddress(UserAddressBO userAddressBO);

    /**
     * 删除地址
     *
     * @param userId
     * @param addressId
     */
    void delete(String userId, String addressId);

    /**
     * 修改默认地址
     *
     * @param userId
     * @param addressId
     */
    void setDefalut(String userId, String addressId);
}
