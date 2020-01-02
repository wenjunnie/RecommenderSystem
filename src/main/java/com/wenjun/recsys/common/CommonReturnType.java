package com.wenjun.recsys.common;

import lombok.Data;

/**
 * @Author: wenjun
 * @Date: 2020/1/2 19:26
 */
@Data
public class CommonReturnType {
    //表明对应请求的返回处理结果
    private String status;
    private Object data;

    //定义一个通用的方法
    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }
}
