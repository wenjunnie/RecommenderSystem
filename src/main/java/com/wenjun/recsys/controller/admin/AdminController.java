package com.wenjun.recsys.controller.admin;

import com.wenjun.recsys.aspect.AdminPermission;
import com.wenjun.recsys.enums.EmBusinessError;
import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: wenjun
 * @Date: 2020/1/3 20:00
 */
@RestController
@RequestMapping("/admin/admin")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class AdminController {

    public static final String CURRENT_ADMIN_SESSION = "CURRENT_ADMIN_SESSION";

    @Value("${admin.passport}")
    private String passport;

    @Value("${admin.password}")
    private String password;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @AdminPermission
    @GetMapping(value = "/home")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("/admin/home");
        modelAndView.addObject("userCount",userService.countAllUser());
        modelAndView.addObject("CONTROLLER_NAME","admin");
        modelAndView.addObject("ACTION_NAME","home");
        return modelAndView;
    }

    @GetMapping(value = "/loginpage")
    public ModelAndView loginPage() {
        ModelAndView modelAndView = new ModelAndView("/admin/login");
        return modelAndView;
    }

    @GetMapping(value = "/login")
    public void login(@RequestParam(name = "passport") String passport,
                      @RequestParam(name = "password") String password) throws BusinessException, IOException {
        if (StringUtils.isEmpty(passport) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户名或密码为空");
        }
        if (passport.equals(this.passport) && BCrypt.checkpw(password,this.password)) {
            //登录成功
            httpServletRequest.getSession().setAttribute(CURRENT_ADMIN_SESSION,passport);
            httpServletResponse.sendRedirect("/admin/admin/home");
        } else {
            //登录失败
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户名或密码错误");
        }
    }
}
