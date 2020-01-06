package com.wenjun.recsys.service;

import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.ShopModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: wenjun
 * @Date: 2020/1/5 20:56
 */
public interface ShopService {
    //创建门店
    ShopModel create(ShopModel shopModel) throws BusinessException;
    //查询某个门店
    ShopModel get(Integer id);
    //查询所有门店
    List<ShopModel> selectAll();
    //统计门店数量
    Integer countAllShop();
    //推荐门店给用户
    List<ShopModel> recommend(BigDecimal longitude, BigDecimal latitude);
}
