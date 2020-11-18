package com.imooc.controller;

import com.imooc.bo.UserBO;
import com.imooc.pojo.Users;
import com.imooc.service.UsersService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户通行证
 */
@Api(value = "注册登录", tags = "用于注册和登录的相关接口")
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UsersService usersService;

    @ApiOperation(value = "用户名是否存在", httpMethod = "GET")
    @GetMapping("usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username) {
        //1.判断用户名不可为空
        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
        //2.查找注册的用户名是否存在
        boolean isExist = usersService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户注册", httpMethod = "POST")
    @PostMapping("regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();
        //1.判断用户名或密码不能为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPassword)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //2.查询用户名是否存在
        boolean isExist = usersService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        //3.判断密码长度不少于6
        if (password.length() < 6) {
            return IMOOCJSONResult.errorMsg("密码长度不能少于6");
        }
        //4.判断两次密码是否一致
        if (!StringUtils.equals(password, confirmPassword)) {
            return IMOOCJSONResult.errorMsg("两次密码不一致");
        }
        Users users = usersService.createUser(userBO);
        //隐藏部分属性
        setNullPreperty(users);
        //设置cookie
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(users), true);
        return IMOOCJSONResult.ok(users);
    }

    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @PostMapping("login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        //1.判断用户名或密码不能为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        String md5Password = MD5Utils.getMD5Str(userBO.getPassword());
        Users users = usersService.queryUserForLogin(userBO.getUsername(), md5Password);
        if (users == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码错误");
        }
        //隐藏部分属性
        setNullPreperty(users);
        //设置cookie
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(users), true);
        return IMOOCJSONResult.ok(users);
    }

    private void setNullPreperty(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
    }

    @ApiOperation(value = "用户退出登录", httpMethod = "GET")
    @PostMapping("logout")
    public IMOOCJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        //设置cookie
        CookieUtils.deleteCookie(request, response, "user");
        // todo 用户退出登录，清除购物差
        // todo 分布式会话，清除登录信息
        return IMOOCJSONResult.ok();
    }

}
