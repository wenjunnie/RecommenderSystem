package com.wenjun.recsys.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenjun.recsys.aspect.AdminPermission;
import com.wenjun.recsys.enums.EmBusinessError;
import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.CategoryModel;
import com.wenjun.recsys.request.CategoryCreateReq;
import com.wenjun.recsys.request.PageQuery;
import com.wenjun.recsys.response.CommonUtil;
import com.wenjun.recsys.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @Author: wenjun
 * @Date: 2020/1/5 14:49
 */
@RestController
@RequestMapping("/admin/category")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    //品类列表
    @AdminPermission
    @GetMapping(value = "/home")
    public ModelAndView home(PageQuery pageQuery) {
        //PageHelper分页
        PageHelper.startPage(pageQuery.getPage(),pageQuery.getSize());
        List<CategoryModel> categoryModelList = categoryService.selectAll();
        PageInfo<CategoryModel> categoryModelPageInfo = new PageInfo<>(categoryModelList);

        ModelAndView modelAndView = new ModelAndView("/category/home");
        modelAndView.addObject("data",categoryModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME","category");
        modelAndView.addObject("ACTION_NAME","home");
        return modelAndView;
    }

    //新增品类界面
    @AdminPermission
    @GetMapping(value = "/createpage")
    public ModelAndView createPage() {
        ModelAndView modelAndView = new ModelAndView("/category/create");
        modelAndView.addObject("CONTROLLER_NAME","category");
        modelAndView.addObject("ACTION_NAME","create");
        return modelAndView;
    }

    //新增品类（不加@RequestBody默认为表单提交）
    @AdminPermission
    @PostMapping(value = "/create")
    public void create(@Valid CategoryCreateReq categoryCreateReq, BindingResult bindingResult) throws BusinessException, IOException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setName(categoryCreateReq.getName());
        categoryModel.setIconUrl(categoryCreateReq.getIconUrl());
        categoryModel.setSort(categoryCreateReq.getSort());
        categoryService.create(categoryModel);
        httpServletResponse.sendRedirect("/admin/category/home");
    }
}
