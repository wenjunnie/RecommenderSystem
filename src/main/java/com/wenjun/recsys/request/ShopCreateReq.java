package com.wenjun.recsys.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: wenjun
 * @Date: 2020/1/5 22:04
 */
@Data
public class ShopCreateReq {
    @NotBlank(message = "门店名不能为空")
    private String name;
    @NotNull(message = "人均价格不能为空")
    @Min(value = 0, message = "人均价格应大于0")
    private BigDecimal pricePerMan;
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;
    @NotNull(message = "门店类型不能为空")
    private Integer categoryId;
    private String tags;
    @NotBlank(message = "营业时间不能为空")
    private String startTime;
    @NotBlank(message = "结业时间不能为空")
    private String endTime;
    @NotBlank(message = "门店地址不能为空")
    private String address;
    @NotNull(message = "商户ID不能为空")
    private Integer sellerId;
    @NotBlank(message = "门店图标不能为空")
    private String iconUrl;
}
