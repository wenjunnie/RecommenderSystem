package com.wenjun.recsys.controller;

import com.wenjun.recsys.response.CommonReturnType;
import com.wenjun.recsys.response.CommonUtil;
import com.wenjun.recsys.enums.EmBusinessError;
import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.UserModel;
import com.wenjun.recsys.request.RegisterReq;
import com.wenjun.recsys.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Author: wenjun
 * @Date: 2020/1/2 17:48
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final String CURRENT_USER_SESSION = "CURRENT_USER_SESSION";

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping(value = "/get")
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        UserModel userModel = userService.getUser(id);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.NO_OBJECT_FOUND);
        }
        return CommonReturnType.create(userModel);
    }

    @PostMapping(value = "/register")
    public CommonReturnType register(@Valid @RequestBody RegisterReq registerReq, BindingResult bindingResult) throws BusinessException {
        //若校验出错则将出错结果返回前端
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }

        UserModel registerUser = new UserModel();
        registerUser.setTelphone(registerReq.getTelphone());
        registerUser.setPassword(registerReq.getPassword());
        registerUser.setNickname(registerReq.getNickname());
        registerUser.setGender(registerReq.getGender());

        UserModel userModel = userService.register(registerUser);
        return CommonReturnType.create(userModel);
    }

    @GetMapping(value = "/login")
    public CommonReturnType login(@RequestParam(name = "telphone") String telphone,
                                  @RequestParam(name = "password") String password) throws BusinessException {
        if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户名或密码为空");
        }
        UserModel userModel = userService.login(telphone,password);
        httpServletRequest.getSession().setAttribute(CURRENT_USER_SESSION,userModel);
        return CommonReturnType.create(userModel);
    }

    @GetMapping(value = "/logout")
    public CommonReturnType logout() {
        httpServletRequest.getSession().invalidate();
        return CommonReturnType.create("用户登出");
    }

    //通过Session获取当前用户信息
    @GetMapping(value = "/getcurrentuser")
    public CommonReturnType getCurrentUser() {
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute(CURRENT_USER_SESSION);
        return CommonReturnType.create(userModel);
    }
}
