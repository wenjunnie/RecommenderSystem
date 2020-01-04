package com.wenjun.recsys.service.impl;

import com.wenjun.recsys.dao.UserModelMapper;
import com.wenjun.recsys.enums.EmBusinessError;
import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.UserModel;
import com.wenjun.recsys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    @Override
    @Transactional
    public UserModel register(UserModel registerUser) throws BusinessException {
        registerUser.setPassword(BCrypt.hashpw(registerUser.getPassword(),BCrypt.gensalt()));
        registerUser.setCreated(new Date());
        registerUser.setUpdated(new Date());
        try {
            userModelMapper.insertSelective(registerUser);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(EmBusinessError.REGISTER_DUP_FAIL);
        }
        return getUser(registerUser.getId());
    }

    @Override
    public UserModel login(String telphone, String password) throws BusinessException {
        UserModel userModel = userModelMapper.selectByTelphone(telphone);
        if (userModel == null || !BCrypt.checkpw(password,userModel.getPassword())) {
            throw new BusinessException(EmBusinessError.LOGIN_FAIL);
        }
        return userModel;
    }

    @Override
    public Integer countAllUser() {
        return userModelMapper.countAllUser();
    }
}
