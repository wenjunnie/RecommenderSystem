package com.wenjun.recsys.enums;

import com.wenjun.recsys.error.CommonError;

/**
 * @Author: wenjun
 * @Date: 2020/1/2 19:48
 */
public enum EmBusinessError implements CommonError {
    NO_OBJECT_FOUND(10001,"请求对象不存在"),
    UNKNOWN_ERROR(10002,"未知错误"),
    NO_HANDLER_FOUND(10003,"没有找到对应的访问路径"),
    BIND_EXCEPTION_ERROR(10004,"请求参数错误"),
    PARAMETER_VALIDATION_ERROR(10005,"请求参数校验失败"),
    REGISTER_DUP_FAIL(20001,"用户已存在"),
    LOGIN_FAIL(20002,"手机号或密码错误"),
    ;

    private int errCode;
    private String errMsg;

    EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
