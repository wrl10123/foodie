package com.imooc.service.impl;

import com.imooc.enums.StatusEnum;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.UserAddressBO;
import com.imooc.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addUserAddress(UserAddressBO userAddressBO) {
        //1.判断当前用户是否存在地址，如果没有，怎新增‘默认地址’
        Integer isDefault = 0;
        List<UserAddress> userAddresses = queryAll(userAddressBO.getUserId());
        if (CollectionUtils.isEmpty(userAddresses)) {
            isDefault = 1;
        }
        //2.保存地址到数据库
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressBO, userAddress);
        userAddress.setId(sid.nextShort());
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(UserAddressBO userAddressBO) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressBO, userAddress);
        userAddress.setId(userAddressBO.getAddressId());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);
        userAddressMapper.delete(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void setDefalut(String userId, String addressId) {
        //1.默认地址改成非默认
        UserAddress uaParam = new UserAddress();
        uaParam.setUserId(userId);
        uaParam.setIsDefault(StatusEnum.YES.type);
        UserAddress userAddress = userAddressMapper.selectOne(uaParam);
        userAddress.setIsDefault(StatusEnum.NO.type);
        userAddressMapper.updateByPrimaryKeySelective(userAddress);

        //2.设置默认地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setUserId(userId);
        defaultAddress.setIsDefault(StatusEnum.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);
        return userAddressMapper.selectOne(userAddress);
    }
}
