package com.imooc.controller.service.impl;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.controller.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Users getUsersInfo(String usersId) {
        return usersMapper.selectByPrimaryKey(usersId);
    }

    @Override
    public int savaUsers(Users users) {
        return usersMapper.insert(users);
    }

    @Override
    public int deleteUsers(String usersId) {
        return usersMapper.deleteByPrimaryKey(usersId);
    }

    @Override
    public int updateUsers(Users users) {
        return usersMapper.updateByPrimaryKey(users);
    }
}
