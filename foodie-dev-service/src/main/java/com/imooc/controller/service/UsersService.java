package com.imooc.controller.service;

import com.imooc.pojo.Users;

public interface UsersService {

    Users getUsersInfo(String usersId);

    int savaUsers(Users users);

    int deleteUsers(String usersId);

    int updateUsers(Users users);

}
