package com.wenjun.recsys.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: wenjun
 * @Date: 2020/1/4 16:15
 */
@Data
public class SellerCreateReq {
    @NotBlank(message = "商户名不能为空")
    private String name;
}
