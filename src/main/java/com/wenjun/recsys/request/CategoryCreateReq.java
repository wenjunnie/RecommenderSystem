package com.wenjun.recsys.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: wenjun
 * @Date: 2020/1/5 14:54
 */
@Data
public class CategoryCreateReq {
    @NotBlank(message = "品类名不能为空")
    private String name;
    @NotBlank(message = "iconUrl不能为空")
    private String iconUrl;
    @NotNull(message = "权重不能为空")
    private Integer sort;
}
