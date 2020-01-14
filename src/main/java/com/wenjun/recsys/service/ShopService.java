package com.wenjun.recsys.service;

import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.ShopModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    //推荐门店给用户（推荐服务V1.0）
    List<ShopModel> recommend(BigDecimal longitude, BigDecimal latitude);
    //搜索门店（搜索服务V1.0）
    List<ShopModel> search(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderby, Integer categoryId, String tags);
    //使用Elasticsearch搜索门店（搜索服务V2.0）
    Map<String,Object> searchES(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderby, Integer categoryId, String tags) throws IOException;
    //查询搜索后的标签集合
    List<Map<String,Object>> searchGroupByTags(String keyword, Integer categoryId, String tags);
}
