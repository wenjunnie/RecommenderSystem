package com.wenjun.recsys.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenjun.recsys.aspect.AdminPermission;
import com.wenjun.recsys.enums.EmBusinessError;
import com.wenjun.recsys.error.BusinessException;
import com.wenjun.recsys.model.SellerModel;
import com.wenjun.recsys.request.PageQuery;
import com.wenjun.recsys.request.SellerCreateReq;
import com.wenjun.recsys.response.CommonReturnType;
import com.wenjun.recsys.response.CommonUtil;
import com.wenjun.recsys.service.SellerService;
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
 * @Date: 2020/1/4 15:08
 */
@RestController
@RequestMapping("/admin/seller")
@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    //商户列表
    @AdminPermission
    @GetMapping(value = "/home")
    public ModelAndView home(PageQuery pageQuery) {
        //PageHelper分页
        PageHelper.startPage(pageQuery.getPage(),pageQuery.getSize());
        List<SellerModel> sellerModelList = sellerService.selectAll();
        PageInfo<SellerModel> sellerModelPageInfo = new PageInfo<>(sellerModelList);

        ModelAndView modelAndView = new ModelAndView("seller/home");
        modelAndView.addObject("data",sellerModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME","seller");
        modelAndView.addObject("ACTION_NAME","home");
        return modelAndView;
    }

    //商户入驻界面
    @AdminPermission
    @GetMapping(value = "/createpage")
    public ModelAndView createPage() {
        ModelAndView modelAndView = new ModelAndView("seller/create");
        modelAndView.addObject("CONTROLLER_NAME","seller");
        modelAndView.addObject("ACTION_NAME","create");
        return modelAndView;
    }

    //创建商家（不加@RequestBody默认为表单提交）
    @AdminPermission
    @PostMapping(value = "/create")
    public void create(@Valid SellerCreateReq sellerCreateReq, BindingResult bindingResult) throws BusinessException, IOException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }
        SellerModel sellerModel = new SellerModel();
        sellerModel.setName(sellerCreateReq.getName());
        sellerService.create(sellerModel);
        httpServletResponse.sendRedirect("/admin/seller/home");
    }

    //禁用商家
    @AdminPermission
    @PostMapping(value = "/down")
    public CommonReturnType down(@RequestParam(name = "id") Integer id) throws BusinessException {
        SellerModel sellerModel = sellerService.changeStatus(id,1);
        return CommonReturnType.create(sellerModel);
    }

    //启用商家
    @AdminPermission
    @PostMapping(value = "/up")
    public CommonReturnType up(@RequestParam(name = "id") Integer id) throws BusinessException {
        SellerModel sellerModel = sellerService.changeStatus(id,0);
        return CommonReturnType.create(sellerModel);
    }
}
