package com.wenjun.recsys.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenjun.recsys.aspect.AdminPermission;
import com.wenjun.recsys.enums.EmBusinessError;
import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.ShopModel;
import com.wenjun.recsys.request.PageQuery;
import com.wenjun.recsys.request.ShopCreateReq;
import com.wenjun.recsys.response.CommonUtil;
import com.wenjun.recsys.service.ShopService;
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
 * @Date: 2020/1/5 22:01
 */
@RestController
@RequestMapping("/admin/shop")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    //品类列表
    @AdminPermission
    @GetMapping(value = "/home")
    public ModelAndView home(PageQuery pageQuery) {
        //PageHelper分页
        PageHelper.startPage(pageQuery.getPage(),pageQuery.getSize());
        List<ShopModel> shopModelList = shopService.selectAll();
        PageInfo<ShopModel> shopModelPageInfo = new PageInfo<>(shopModelList);

        ModelAndView modelAndView = new ModelAndView("shop/home");
        modelAndView.addObject("data",shopModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME","shop");
        modelAndView.addObject("ACTION_NAME","home");
        return modelAndView;
    }

    //新增品类界面
    @AdminPermission
    @GetMapping(value = "/createpage")
    public ModelAndView createPage() {
        ModelAndView modelAndView = new ModelAndView("shop/create");
        modelAndView.addObject("CONTROLLER_NAME","shop");
        modelAndView.addObject("ACTION_NAME","create");
        return modelAndView;
    }

    //新增品类（不加@RequestBody默认为表单提交）
    @AdminPermission
    @PostMapping(value = "/create")
    public void create(@Valid ShopCreateReq shopCreateReq, BindingResult bindingResult) throws BusinessException, IOException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }
        ShopModel shopModel = new ShopModel();
        shopModel.setName(shopCreateReq.getName());
        shopModel.setPricePerMan(shopCreateReq.getPricePerMan().intValue());
        shopModel.setLatitude(shopCreateReq.getLatitude());
        shopModel.setLongitude(shopCreateReq.getLongitude());
        shopModel.setCategoryId(shopCreateReq.getCategoryId());
        shopModel.setTags(shopCreateReq.getTags());
        shopModel.setStartTime(shopCreateReq.getStartTime());
        shopModel.setEndTime(shopCreateReq.getEndTime());
        shopModel.setAddress(shopCreateReq.getAddress());
        shopModel.setSellerId(shopCreateReq.getSellerId());
        shopModel.setIconUrl(shopCreateReq.getIconUrl());
        shopService.create(shopModel);
        httpServletResponse.sendRedirect("/admin/shop/home");
    }
}
