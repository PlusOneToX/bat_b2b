package com.bat.distributor.web.platform;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.platform.PlatformServiceI;
import com.bat.distributor.api.platform.dto.PlatformCmd;
import com.bat.distributor.api.platform.dto.PlatformListQry;
import com.bat.distributor.api.platform.dto.PlatformOpenCmd;
import com.bat.distributor.api.platform.dto.data.PlatformDTO;
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
@Api(tags = "分销商平台接口", value = "PlatformController")
@RestController
@RequestMapping("/distributor/v1/web/admin/platform")
public class PlatformController {
    private static Logger logger = LoggerFactory.getLogger(PlatformController.class);

    @Resource
    private PlatformServiceI service;

    @ApiOperation(value = "根据搜索条件查找分销商平台列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<PlatformDTO>> listPlatform(@Valid PlatformListQry qry) {
        PageInfo<PlatformDTO> pageInfo = service.listPlatform(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找分销商平台列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<PlatformDTO>> listPlatformPo(@Valid PlatformListQry qry) {
        PageInfo<PlatformDTO> pageInfo = service.listPlatform(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据分销商平台id获取分销商平台详情")
    @GetMapping()
    public Response<PlatformDTO> getPlatform(@Valid BaseId qry) {
        PlatformDTO dto = service.getPlatform(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Platform, value = CommonLogTypeConstantDTO.PlatformAdd)
    @ApiOperation(value = "添加分销商平台")
    @PostMapping()
    public Response createPlatform(@RequestBody @Valid PlatformCmd cmd) {
        service.createPlatform(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Platform, value = CommonLogTypeConstantDTO.PlatformUpdate)
    @ApiOperation(value = "更新分销商平台")
    @PutMapping()
    public Response updatePlatform(@RequestBody @Valid PlatformCmd cmd) {
        service.updatePlatform(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Platform, value = CommonLogTypeConstantDTO.PlatformUpdateStatus)
    @ApiOperation(value = "更新分销商平台状态")
    @PutMapping(value = "/open")
    public Response openPlatform(@RequestBody @Valid PlatformOpenCmd cmd) {
        service.openPlatform(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Platform, value = CommonLogTypeConstantDTO.PlatformDeleteById)
    @ApiOperation(value = "根据分销商平台id删除分销商平台")
    @DeleteMapping()
    public Response deletePlatform(@RequestBody @Valid BaseId cmd) {
        service.deletePlatform(cmd);
        return Response.buildSuccess();
    }
}
