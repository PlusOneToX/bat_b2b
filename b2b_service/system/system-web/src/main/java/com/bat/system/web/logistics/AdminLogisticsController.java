package com.bat.system.web.logistics;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.logistics.dto.*;
import com.bat.system.web.annotation.SysLog;
import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.logistics.LogisticsService;
import com.bat.system.api.logistics.dto.*;
import com.bat.system.api.logistics.dto.data.FormulaCheckDTO;
import com.bat.system.api.logistics.dto.data.LogisticsDTO;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/9 19:10
 */
@Api(tags = "系统模块-配送后台接口", value = "AdminLogisticsController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/logistics")
public class AdminLogisticsController extends BaseController {

    @Resource
    private LogisticsService logisticsService;

    @GetMapping("/list")
    @ApiOperation(value = "查询配送列表(分页)")
    public Response<PageInfo<LogisticsDTO>> listLogistics(@Valid LogisticsQry qry) {
        PageInfo pageInfo = logisticsService.listLogistics(qry);
        return Response.of(pageInfo);
    }

    @GetMapping("/po/list")
    @ApiOperation(value = "查询配送列表(分页)")
    public Response<PageInfo<LogisticsDTO>> listLogisticsPo(@Valid LogisticsQry qry) {
        PageInfo pageInfo = logisticsService.listLogistics(qry);
        return Response.of(pageInfo);
    }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个配送")
    public Response<LogisticsDTO> getLogistics(@Valid LogisticsId id) {
        return Response.of(logisticsService.getLogisticsById(id.getId()));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminLogistics, value = CommonLogTypeConstantDTO.AdminLogisticsAdd)
    @PostMapping()
    @ApiOperation(value = "添加配送")
    public Response createLogistics(@RequestBody @Valid LogisticsCreateCmd cmd) {
        logisticsService.createLogistics(cmd);
        return Response.buildSuccess();
    }

    @PostMapping("/formula")
    @ApiOperation(value = "公式校验")
    public Response<FormulaCheckDTO> verificationDistributionFormula(@RequestBody @Valid FormulaCheckCmd cmd) {
        return Response.of(logisticsService.verificationDistributionFormula(cmd));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminLogistics, value = CommonLogTypeConstantDTO.AdminLogisticsUpdate)
    @PutMapping()
    @ApiOperation(value = "更新配送")
    public Response updateLogistics(@RequestBody @Valid LogisticsUpdateCmd cmd) {
        return Response.of(logisticsService.updateLogistics(cmd));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminLogistics, value = CommonLogTypeConstantDTO.AdminLogisticsDelete)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除配送")
    public Response deleteLogistics(@RequestBody @Valid LogisticsId id) {
        logisticsService.deleteLogisticsById(id.getId());
        return Response.buildSuccess();
    }

}
