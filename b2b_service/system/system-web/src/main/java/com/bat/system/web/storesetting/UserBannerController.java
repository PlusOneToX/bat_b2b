package com.bat.system.web.storesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.storesetting.BannerService;
import com.bat.system.api.storesetting.dto.BannerQry;
import com.bat.system.api.storesetting.dto.data.BannerDTO;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 首页推广（轮播图）（banner）
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-首页推广前台接口", value = "UserBannerController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/user/banner")
public class UserBannerController extends BaseController {

    @Resource
    private BannerService bannerService;

    @GetMapping("/list")
    @ApiOperation(value = "查询推广列表(分页)")
    public Response<PageInfo<BannerDTO>> listBanner(@Valid BannerQry qry) {
        return Response.of(bannerService.listBanner(qry));
    }

}
