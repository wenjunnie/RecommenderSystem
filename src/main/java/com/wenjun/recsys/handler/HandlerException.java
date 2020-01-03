package com.wenjun.recsys.handler;

import com.wenjun.recsys.response.CommonReturnType;
import com.wenjun.recsys.enums.EmBusinessError;
import com.wenjun.recsys.error.BusinessException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一异常处理器
 * @Author: wenjun
 * @Date: 2020/1/2 20:28
 */
@ControllerAdvice
public class HandlerException {
    @ExceptionHandler(Exception.class)
    @ResponseBody//不要忘了加
    public Object handler(HttpServletRequest request, HttpServletResponse httpServletResponse, Exception e) {
        e.printStackTrace();//在控制台打印异常信息
        Map<String, Object> map = new HashMap<>();
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            map.put("errCode", businessException.getErrCode());
            map.put("errMsg", businessException.getErrMsg());
        } else if (e instanceof ServletRequestBindingException) {//传参错误（405）
            map.put("errCode", EmBusinessError.BIND_EXCEPTION_ERROR.getErrCode());
            map.put("errMsg", EmBusinessError.BIND_EXCEPTION_ERROR.getErrMsg());
        } else if (e instanceof NoHandlerFoundException) {//页面404
            map.put("errCode", EmBusinessError.NO_HANDLER_FOUND.getErrCode());
            map.put("errMsg", EmBusinessError.NO_HANDLER_FOUND.getErrMsg());
        } else {
            map.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            map.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }
        return CommonReturnType.create(map,"fail");
    }
}
