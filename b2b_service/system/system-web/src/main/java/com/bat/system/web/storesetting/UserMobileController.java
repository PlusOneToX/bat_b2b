package com.bat.system.web.storesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.storesetting.MobileService;
import com.bat.system.api.storesetting.dto.MobileModuleType;
import com.bat.system.api.storesetting.dto.MobileQry;
import com.bat.system.api.storesetting.dto.data.MobileDTO;
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
 * @description: 首页移动端首页
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-移动端首页前台接口", value = "UserMobileController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/user/mobile")
public class UserMobileController extends BaseController {

    @Resource
    private MobileService mobileService;

    @GetMapping("/list")
    @ApiOperation(value = "查询移动端首页列表(分页)")
    public Response<PageInfo<MobileDTO>> listMobile(@Valid MobileQry qry) {
        return Response.of(mobileService.listMobile(qry));
    }

    @GetMapping()
    @ApiOperation(value = "通过模块类型查询移动端首页")
    public Response<MobileDTO> getMobile(@Valid MobileModuleType moduleType) {
        return Response.of(mobileService.getMobileByModuleType(moduleType.getModuleType()));
    }

}
