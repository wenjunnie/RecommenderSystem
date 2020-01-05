package com.wenjun.recsys.service.impl;

import com.wenjun.recsys.dao.ShopModelMapper;
import com.wenjun.recsys.enums.EmBusinessError;
import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.CategoryModel;
import com.wenjun.recsys.model.SellerModel;
import com.wenjun.recsys.model.ShopModel;
import com.wenjun.recsys.service.CategoryService;
import com.wenjun.recsys.service.SellerService;
import com.wenjun.recsys.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: wenjun
 * @Date: 2020/1/5 20:57
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopModelMapper shopModelMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SellerService sellerService;

    @Override
    @Transactional
    public ShopModel create(ShopModel shopModel) throws BusinessException {
        shopModel.setCreated(new Date());
        shopModel.setUpdated(new Date());
        //校验商户是否存在
        SellerModel sellerModel = sellerService.get(shopModel.getSellerId());
        if (sellerModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商户不存在");
        }
        //校验商户是否被禁用
        if (sellerModel.getDisabledFlag() == 1) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商户已被禁用");
        }
        //校验类目
        CategoryModel categoryModel = categoryService.get(shopModel.getCategoryId());
        if (categoryModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"类目不存在");
        }
        shopModelMapper.insertSelective(shopModel);
        return get(shopModel.getId());
    }

    @Override
    public ShopModel get(Integer id) {
        ShopModel shopModel = shopModelMapper.selectByPrimaryKey(id);
        if (shopModel == null) {
            return null;
        }
        shopModel.setSellerModel(sellerService.get(shopModel.getSellerId()));
        shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        return shopModel;
    }

    @Override
    public List<ShopModel> selectAll() {
        List<ShopModel> shopModelList = shopModelMapper.selectAll();
        shopModelList.forEach(shopModel -> {
            shopModel.setSellerModel(sellerService.get(shopModel.getSellerId()));
            shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        });
        return shopModelList;
    }

    @Override
    public Integer countAllShop() {
        return shopModelMapper.countAllShop();
    }
}
