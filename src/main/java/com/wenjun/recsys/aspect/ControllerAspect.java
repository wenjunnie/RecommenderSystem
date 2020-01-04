package com.wenjun.recsys.aspect;

import com.wenjun.recsys.controller.admin.AdminController;
import com.wenjun.recsys.enums.EmBusinessError;
import com.wenjun.recsys.response.CommonReturnType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 在controller层的切面
 * @Author: wenjun
 * @Date: 2020/1/4 10:34
 */
@Aspect
@Configuration
public class ControllerAspect {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Around("execution(* com.wenjun.recsys.controller.admin.*.*(..)) && @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object adminControllerBeforeValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AdminPermission adminPermission = method.getAnnotation(AdminPermission.class);
        if (adminPermission == null) {
            //公共方法
            Object resultObject = joinPoint.proceed();
            return resultObject;
        }
        //判断当前管理员是否登录
        String passport = (String) httpServletRequest.getSession().getAttribute(AdminController.CURRENT_ADMIN_SESSION);
        if (passport == null) {
            //未登录
            //如果是返回html页面
            if (adminPermission.produceType().equals("text/html")) {
                httpServletResponse.sendRedirect("/admin/admin/loginpage");
                return null;
            } else {
                Map<String,Object> map = new HashMap<>();
                map.put("errCode", EmBusinessError.ADMIN_NOT_LOGIN.getErrCode());
                map.put("errMsg", EmBusinessError.ADMIN_NOT_LOGIN.getErrMsg());
                return CommonReturnType.create(map,"fail");
            }
        } else {
            Object resultObject = joinPoint.proceed();
            return resultObject;
        }
    }
}
