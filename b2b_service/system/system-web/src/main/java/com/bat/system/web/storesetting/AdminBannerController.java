package com.bat.system.web.storesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.storesetting.BannerService;
import com.bat.system.api.storesetting.dto.*;
import com.bat.system.api.storesetting.dto.data.BannerDTO;
import com.bat.system.web.annotation.SysLog;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.storesetting.dto.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 首页推广（轮播图）（banner）
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-首页推广后台接口", value = "AdminBannerController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/banner")
public class AdminBannerController extends BaseController {

    @Resource
    private BannerService bannerService;

    @GetMapping("/list")
    @ApiOperation(value = "查询推广列表(分页)")
    public Response<PageInfo<BannerDTO>> listBanner(@Valid BannerQry qry) {
        return Response.of(bannerService.listBanner(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminBanner, value = CommonLogTypeConstantDTO.AdminBannerAdd)
    @PostMapping()
    @ApiOperation(value = "添加推广")
    public Response createBanner(@RequestBody @Valid BannerCreateCmd cmd) {
        bannerService.createBanner(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminBanner, value = CommonLogTypeConstantDTO.AdminBannerUp)
    @PutMapping("/sort/up")
    @ApiOperation(value = "上移")
    public Response sortBannerUp(@RequestBody @Valid BannerId id) {
        bannerService.sortBannerUp(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminBanner, value = CommonLogTypeConstantDTO.AdminBannerDown)
    @PutMapping("/sort/down")
    @ApiOperation(value = "下移")
    public Response sortBannerDown(@RequestBody @Valid BannerId id) {
        bannerService.sortBannerDown(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminBanner, value = CommonLogTypeConstantDTO.AdminBannerUpdate)
    @PutMapping()
    @ApiOperation(value = "更新推广")
    public Response updateBanner(@RequestBody @Valid BannerUpdateCmd cmd) {
        cmd.setSort(null);
        bannerService.updateBanner(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminBanner, value = CommonLogTypeConstantDTO.AdminBannerDelete)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除推广")
    public Response deleteBanner(@RequestBody @Valid BannerId id) {
        bannerService.deleteBannerById(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminBanner, value = CommonLogTypeConstantDTO.AdminBannerDeleteByIds)
    @DeleteMapping("/ids")
    @ApiOperation(value = "通过IDS删除推广")
    public Response deleteBannerByIds(@RequestBody @Valid BannerIds ids) {
        ids.getIds().forEach(integer -> bannerService.deleteBannerById(integer));
        return Response.buildSuccess();
    }
}
