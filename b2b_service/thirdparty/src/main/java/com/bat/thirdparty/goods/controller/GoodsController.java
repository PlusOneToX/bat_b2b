package com.bat.thirdparty.goods.controller;

import javax.annotation.Resource;

import com.bat.thirdparty.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bat.thirdparty.goods.api.GoodsServiceI;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/thirdparty/v1/web/admin/goods")
@Api(tags = "商品第三方调用后台接口")
public class GoodsController extends BaseController {

    @Resource
    private GoodsServiceI service;

}
