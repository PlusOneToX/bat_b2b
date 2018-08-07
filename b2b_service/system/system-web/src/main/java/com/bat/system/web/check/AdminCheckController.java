package com.bat.system.web.check;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.system.web.annotation.SysLog;
import org.springframework.web.bind.annotation.*;

import com.bat.system.api.check.CheckService;
import com.bat.system.api.check.dto.CheckConfigUpdateCmd;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 按照以前的逻辑 代码不修改
 * @date: 2018/4/12 12:15
 */
@Api(tags = "系统模块-审批配置后台接口（按照以前的逻辑 代码不修改）", value = "AdminCheckController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/check")
public class AdminCheckController extends BaseController {

    @Resource
    private CheckService checkService;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCheck, value = CommonLogTypeConstantDTO.AdminCheckEditCheckConfig)
    @PutMapping()
    @ApiOperation(value = "审批配置修改")
    public Response editCheckConfig(@RequestBody @Valid CheckConfigUpdateCmd cmd) {
        checkService.editCheckConfig(cmd);
        return Response.buildSuccess();
    }

    @GetMapping("/checkDetail")
    @ApiOperation(value = "查询审批配置列表(不分页)")
    public Response checkDetail() {
        return Response.of(checkService.checkDetail());
    }

    @GetMapping("/po/checkDetail")
    @ApiOperation(value = "查询审批配置列表(不分页)(公共数据)")
    public Response checkPoDetail() {
        return Response.of(checkService.checkDetail());
    }

}
