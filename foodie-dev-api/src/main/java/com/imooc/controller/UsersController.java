package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.controller.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @RequestMapping("getUser")
    public Object getUser(String usersId){
        return usersService.getUsersInfo(usersId);
    }

    @RequestMapping("saveUser")
    public Object saveUser(Users users){
        return usersService.savaUsers(users);
    }

    @RequestMapping("deleteUsers")
    public Object deleteUsers(String usersId){
        return usersService.deleteUsers(usersId);
    }

    @RequestMapping("updateUsers")
    public Object updateUsers(Users users){
        return usersService.updateUsers(users);
    }


}
