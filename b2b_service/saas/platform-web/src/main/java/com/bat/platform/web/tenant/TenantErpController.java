package com.bat.platform.web.tenant;

import com.bat.platform.api.base.common.IdDTO;
import com.bat.platform.api.tenant.TenantErpServiceI;
import com.bat.platform.api.tenant.dto.TenantErpCmd;
import com.bat.platform.api.tenant.dto.data.TenantErpDTO;
import com.bat.platform.web.base.BaseController;
import com.bat.platform.web.base.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "平台租户ERP配置接口", value = "TenantErpController")
@RestController
@RequestMapping("/platform/v1/web/tenant/erp")
public class TenantErpController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(TenantErpController.class);

    @Resource
    private TenantErpServiceI service;

    @ApiOperation(value = "获取配置")
    @GetMapping(value = "/config")
    public Response<TenantErpDTO> config(@Valid IdDTO idDTO) {
        TenantErpDTO tenantErpDTO= service.config(idDTO.getId());
        return Response.of(tenantErpDTO);
    }

    @ApiOperation(value = "添加配置")
    @PostMapping(value = "/add")
    public Response add(@Valid @RequestBody TenantErpCmd cmd) {
        service.add(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "更新配置")
    @PutMapping(value = "/update")
    public Response update(@Valid @RequestBody TenantErpCmd cmd) {
        service.update(cmd);
        return Response.buildSuccess();
    }
}
