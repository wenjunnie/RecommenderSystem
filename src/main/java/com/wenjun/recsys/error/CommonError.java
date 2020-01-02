package com.wenjun.recsys.error;

/**
 * @Author: wenjun
 * @Date: 2020/1/2 19:32
 */
public interface CommonError {
    int getErrCode();
    String getErrMsg();
    CommonError setErrMsg(String errMsg);
}
