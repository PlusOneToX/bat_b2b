package com.bat.distributor.web.platform;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.platform.SysPlatformServiceI;
import com.bat.distributor.api.platform.dto.SysPlatformCmd;
import com.bat.distributor.api.platform.dto.SysPlatformListQry;
import com.bat.distributor.api.platform.dto.data.SysPlatformDTO;
import com.bat.distributor.api.platform.dto.data.SysPlatformListDTO;
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
@Api(tags = "分销商系统平台接口", value = "SysPlatformController")
@RestController
@RequestMapping("/distributor/v1/web/admin/sysplatform")
public class SysPlatformController {
    private static Logger logger = LoggerFactory.getLogger(SysPlatformController.class);

    @Resource
    private SysPlatformServiceI service;

    @ApiOperation(value = "根据搜索条件查找分销商系统平台列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<SysPlatformListDTO>> listSysPlatform(@Valid SysPlatformListQry qry) {
        PageInfo<SysPlatformListDTO> pageInfo = service.listSysPlatform(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找分销商系统平台列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<SysPlatformListDTO>> listSysPlatformPo(@Valid SysPlatformListQry qry) {
        PageInfo<SysPlatformListDTO> pageInfo = service.listSysPlatform(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据分销商系统平台id获取分销商系统平台详情")
    @GetMapping()
    public Response<SysPlatformDTO> getSysPlatform(@Valid BaseId qry) {
        SysPlatformDTO dto = service.getSysPlatform(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.SysPlatform, value = CommonLogTypeConstantDTO.SysPlatformAdd)
    @ApiOperation(value = "添加分销商系统平台")
    @PostMapping()
    public Response createSysPlatform(@RequestBody @Valid SysPlatformCmd cmd) {
        service.createSysPlatform(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.SysPlatform, value = CommonLogTypeConstantDTO.SysPlatformUpdate)
    @ApiOperation(value = "更新分销商系统平台")
    @PutMapping()
    public Response updateSysPlatform(@RequestBody @Valid SysPlatformCmd cmd) {
        service.updateSysPlatform(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.SysPlatform, value = CommonLogTypeConstantDTO.SysPlatformDeleteById)
    @ApiOperation(value = "根据分销商系统平台id删除分销商系统平台")
    @DeleteMapping()
    public Response deleteSysPlatform(@RequestBody @Valid BaseId cmd) {
        service.deleteSysPlatform(cmd);
        return Response.buildSuccess();
    }
}
