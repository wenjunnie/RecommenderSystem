package com.wenjun.recsys.service;

import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.CategoryModel;

import java.util.List;

/**
 * @Author: wenjun
 * @Date: 2020/1/5 14:39
 */
public interface CategoryService {
    //创建品类
    CategoryModel create(CategoryModel categoryModel) throws BusinessException;
    //查询某个品类
    CategoryModel get(Integer id);
    //查询所有品类
    List<CategoryModel> selectAll();
}
