package com.wenjun.recsys.service;

import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.SellerModel;

import java.util.List;

/**
 * @Author: wenjun
 * @Date: 2020/1/4 15:03
 */
public interface SellerService {
    //新增商家
    SellerModel create(SellerModel sellerModel);
    //查询某个商家
    SellerModel get(Integer id);
    //查询所有商家
    List<SellerModel> selectAll();
    //设置商家启用/禁用状态
    SellerModel changeStatus(Integer id, Integer disabledFlag) throws BusinessException;
}
