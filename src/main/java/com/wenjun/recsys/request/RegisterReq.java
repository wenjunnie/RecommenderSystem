package com.wenjun.recsys.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: wenjun
 * @Date: 2020/1/3 14:00
 */
@Data
public class RegisterReq {
    @NotBlank(message = "手机号不能为空")
    private String telphone;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    @NotNull(message = "性别不能为空")
    private Integer gender;
}
