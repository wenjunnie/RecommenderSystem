package com.wenjun.recsys.service.impl;

import com.wenjun.recsys.dao.CategoryModelMapper;
import com.wenjun.recsys.enums.EmBusinessError;
import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.CategoryModel;
import com.wenjun.recsys.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: wenjun
 * @Date: 2020/1/5 14:41
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryModelMapper categoryModelMapper;

    @Override
    @Transactional
    public CategoryModel create(CategoryModel categoryModel) throws BusinessException {
        categoryModel.setCreated(new Date());
        categoryModel.setUpdated(new Date());
        //唯一索引需catch异常
        try {
            categoryModelMapper.insertSelective(categoryModel);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(EmBusinessError.CATEGORY_NAME_DUPLICATED);
        }
        return get(categoryModel.getId());
    }

    @Override
    public CategoryModel get(Integer id) {
        return categoryModelMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CategoryModel> selectAll() {
        return categoryModelMapper.selectAll();
    }

    @Override
    public Integer countAllCategory() {
        return categoryModelMapper.countAllCategory();
    }
}
