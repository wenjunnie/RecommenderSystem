package com.wenjun.recsys.service.impl;

import com.wenjun.recsys.dao.UserModelMapper;
import com.wenjun.recsys.model.UserModel;
import com.wenjun.recsys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: wenjun
 * @Date: 2020/1/2 17:55
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserModelMapper userModelMapper;

    @Override
    public UserModel getUser(Integer id) {
        return userModelMapper.selectByPrimaryKey(id);
    }
}
