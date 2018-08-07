package com.bat.system.web.storesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.storesetting.MobileService;
import com.bat.system.api.storesetting.dto.*;
import com.bat.system.api.storesetting.dto.data.MobileDTO;
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
 * @description: 首页移动端首页
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-移动端首页后台接口", value = "AdminMobileController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/mobile")
public class AdminMobileController extends BaseController {

    @Resource
    private MobileService mobileService;

    @GetMapping("/list")
    @ApiOperation(value = "查询移动端首页列表(分页)")
    public Response<PageInfo<MobileDTO>> listMobile(@Valid MobileQry qry) {
        return Response.of(mobileService.listMobile(qry));
    }

    @GetMapping()
    @ApiOperation(value = "通过id查询移动端首页")
    public Response<MobileDTO> getMobile(@Valid MobileId id) {
        return Response.of(mobileService.getMobileById(id.getId()));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMobile, value = CommonLogTypeConstantDTO.AdminMobileAdd)
    @PostMapping()
    @ApiOperation(value = "添加移动端首页")
    public Response createMobile(@RequestBody @Valid MobileCreateCmd cmd) {
        mobileService.createMobile(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMobile, value = CommonLogTypeConstantDTO.AdminMobileUpdate)
    @PutMapping()
    @ApiOperation(value = "更新移动端首页")
    public Response updateMobile(@RequestBody @Valid MobileUpdateCmd cmd) {
        mobileService.updateMobile(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMobile, value = CommonLogTypeConstantDTO.AdminMobileReleaseById)
    @PutMapping("/releaseById")
    @ApiOperation(value = "发布移动端首页(显示隐藏)")
    public Response releaseMobileById(@RequestBody @Valid MobileReleaseCmd cmd) {
        mobileService.releaseMobile(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMobile, value = CommonLogTypeConstantDTO.AdminMobileReleaseByIds)
    @PutMapping("/releaseByIds")
    @ApiOperation(value = "发布移动端首页(显示隐藏)")
    public Response releaseMobileByIds(@RequestBody @Valid MobileReleaseIdsCmd cmd) {
        cmd.getIds().forEach(integer -> {
            MobileReleaseCmd mobileReleaseCmd = new MobileReleaseCmd();
            mobileReleaseCmd.setId(integer);
            mobileReleaseCmd.setReleaseStatus(cmd.getReleaseStatus());
            mobileService.releaseMobile(mobileReleaseCmd);
        });
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMobile, value = CommonLogTypeConstantDTO.AdminMobileDelete)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除移动端首页")
    public Response deleteMobile(@RequestBody @Valid MobileId id) {
        mobileService.deleteMobileById(id.getId());
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMobile, value = CommonLogTypeConstantDTO.AdminMobileDeleteByMobileItem)
    @DeleteMapping("/item")
    @ApiOperation(value = "通过itemID删除移动端首页子项")
    public Response deleteMobileItem(@RequestBody @Valid MobileId id) {
        mobileService.deleteMobileItemById(id.getId());
        return Response.buildSuccess();
    }
}
