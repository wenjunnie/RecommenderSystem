package com.wenjun.recsys.service;

import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.UserModel;

/**
 * @Author: wenjun
 * @Date: 2020/1/2 17:48
 */
public interface UserService {
    //通过ID获取某个用户信息
    UserModel getUser(Integer id);
    //用户注册
    UserModel register(UserModel registerUser) throws BusinessException;
    //用户登录
    UserModel login(String telphone, String password) throws BusinessException;
    //注册用户数量
    Integer countAllUser();
}
