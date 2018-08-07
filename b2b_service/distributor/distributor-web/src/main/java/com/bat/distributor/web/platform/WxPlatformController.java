package com.bat.distributor.web.platform;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.platform.WxPlatformServiceI;
import com.bat.distributor.api.platform.dto.WxPlatformCmd;
import com.bat.distributor.api.platform.dto.WxPlatformListQry;
import com.bat.distributor.api.platform.dto.data.WxPlatformDTO;
import com.bat.distributor.api.platform.dto.data.WxPlatformListDTO;
import com.bat.distributor.web.constants.CommonLogTypeConstantDTO;
import com.bat.distributor.web.annotation.SysLog;
import com.bat.distributor.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/10 8:17
 */
@Api(tags = "分销商微信平台接口", value = "WxPlatformController")
@RestController
@RequestMapping("/distributor/v1/web/admin/wxplatform")
public class WxPlatformController {
    private static Logger logger = LoggerFactory.getLogger(WxPlatformController.class);

    @Resource
    private WxPlatformServiceI service;

    @ApiOperation(value = "根据搜索条件查找分销商微信平台列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<WxPlatformListDTO>> listWxPlatform(@Valid WxPlatformListQry qry) {
        PageInfo<WxPlatformListDTO> pageInfo = service.listWxPlatform(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找分销商微信平台列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<WxPlatformListDTO>> listWxPlatformPo(@Valid WxPlatformListQry qry) {
        PageInfo<WxPlatformListDTO> pageInfo = service.listWxPlatform(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据分销商微信平台id获取分销商微信平台详情")
    @GetMapping()
    public Response<WxPlatformDTO> getWxPlatform(@Valid BaseId qry) {
        WxPlatformDTO dto = service.getWxPlatform(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.WxPlatform, value = CommonLogTypeConstantDTO.WxPlatformAdd)
    @ApiOperation(value = "添加分销商微信平台")
    @PostMapping()
    public Response createWxPlatform(@RequestBody @Valid WxPlatformCmd cmd) {
        service.createWxPlatform(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.WxPlatform, value = CommonLogTypeConstantDTO.WxPlatformUpdate)
    @ApiOperation(value = "更新分销商微信平台")
    @PutMapping()
    public Response updateWxPlatform(@RequestBody @Valid WxPlatformCmd cmd) {
        service.updateWxPlatform(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.WxPlatform, value = CommonLogTypeConstantDTO.WxPlatformDeleteById)
    @ApiOperation(value = "根据分销商微信平台id删除分销商微信平台")
    @DeleteMapping()
    public Response deleteWxPlatform(@RequestBody @Valid BaseId cmd) {
        service.deleteWxPlatform(cmd);
        return Response.buildSuccess();
    }
}
