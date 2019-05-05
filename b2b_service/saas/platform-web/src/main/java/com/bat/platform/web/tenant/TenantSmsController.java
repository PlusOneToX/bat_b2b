package com.bat.platform.web.tenant;

import com.bat.platform.api.tenant.TenantSmsServiceI;
import com.bat.platform.api.tenant.dto.TenantSmsCmd;
import com.bat.platform.api.tenant.dto.TenantSmsQry;
import com.bat.platform.api.tenant.dto.data.TenantSmsDTO;
import com.bat.platform.web.base.BaseController;
import com.bat.platform.web.base.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "平台租户短信配置接口", value = "TenantSmsController")
@RestController
@RequestMapping("/platform/v1/web/tenant/sms")
public class TenantSmsController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(TenantSmsController.class);

    @Resource
    private TenantSmsServiceI service;

    @ApiOperation(value = "获取配置")
    @GetMapping(value = "/config")
    public Response<TenantSmsDTO> config(@Valid TenantSmsQry qry) {
        TenantSmsDTO tenantSmsDTO = service.config(qry);
        return Response.of(tenantSmsDTO);
    }

    @ApiOperation(value = "添加配置")
    @PostMapping(value = "/add")
    public Response add(@Valid @RequestBody TenantSmsCmd cmd) {
        service.add(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "更新配置")
    @PutMapping(value = "/update")
    public Response update(@Valid @RequestBody TenantSmsCmd cmd) {
        service.update(cmd);
        return Response.buildSuccess();
    }
}
