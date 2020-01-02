package com.wenjun.recsys.service;

import com.wenjun.recsys.model.UserModel;

/**
 * @Author: wenjun
 * @Date: 2020/1/2 17:48
 */
public interface UserService {
    UserModel getUser(Integer id);
}
