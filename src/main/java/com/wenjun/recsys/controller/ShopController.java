package com.wenjun.recsys.controller;

import com.wenjun.recsys.enums.EmBusinessError;
import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.ShopModel;
import com.wenjun.recsys.response.CommonReturnType;
import com.wenjun.recsys.service.CategoryService;
import com.wenjun.recsys.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: wenjun
 * @Date: 2020/1/6 12:09
 */
@RestController("shop")
@RequestMapping("/shop")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;

    //推荐门店给用户
    @GetMapping(value = "/recommend")
    public CommonReturnType recommend(@RequestParam(name = "longitude")BigDecimal longitude,
                                      @RequestParam(name = "latitude") BigDecimal latitude) throws BusinessException {
        if (longitude == null || latitude == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        List<ShopModel> shopModelList = shopService.recommend(longitude,latitude);
        return CommonReturnType.create(shopModelList);
    }
}
