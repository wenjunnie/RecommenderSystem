package com.wenjun.recsys.controller;

import com.wenjun.recsys.model.CategoryModel;
import com.wenjun.recsys.response.CommonReturnType;
import com.wenjun.recsys.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: wenjun
 * @Date: 2020/1/5 15:18
 */
@RestController("category")
@RequestMapping("/category")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/list")
    public CommonReturnType list() {
        List<CategoryModel> categoryModelList = categoryService.selectAll();
        return CommonReturnType.create(categoryModelList);
    }
}
