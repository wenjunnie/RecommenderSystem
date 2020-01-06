package com.wenjun.recsys.controller;

import com.wenjun.recsys.enums.EmBusinessError;
import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.CategoryModel;
import com.wenjun.recsys.model.ShopModel;
import com.wenjun.recsys.response.CommonReturnType;
import com.wenjun.recsys.service.CategoryService;
import com.wenjun.recsys.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //推荐门店给用户（推荐服务V1.0）
    @GetMapping(value = "/recommend")
    public CommonReturnType recommend(@RequestParam(name = "longitude")BigDecimal longitude,
                                      @RequestParam(name = "latitude") BigDecimal latitude) throws BusinessException {
        if (longitude == null || latitude == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        List<ShopModel> shopModelList = shopService.recommend(longitude,latitude);
        return CommonReturnType.create(shopModelList);
    }

    //搜索门店（搜索服务V1.0）
    @GetMapping(value = "/search")
    public CommonReturnType search(@RequestParam(name = "longitude")BigDecimal longitude,
                                   @RequestParam(name = "latitude") BigDecimal latitude,
                                   @RequestParam(name = "keyword", required = false) String keyword,
                                   @RequestParam(name = "orderby", required = false) Integer orderby,
                                   @RequestParam(name = "categoryId", required = false) Integer categoryId,
                                   @RequestParam(name = "tags", required = false) String tags) throws BusinessException {
        if (longitude == null || latitude == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        List<ShopModel> shopModelList = shopService.search(longitude,latitude,keyword,orderby,categoryId,tags);
        List<CategoryModel> categoryModelList = categoryService.selectAll();
        List<Map<String,Object>> tagsGroup = shopService.searchGroupByTags(keyword,categoryId,tags);
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("shop",shopModelList);
        resMap.put("category",categoryModelList);
        resMap.put("tags",tagsGroup);
        return CommonReturnType.create(resMap);
    }
}
