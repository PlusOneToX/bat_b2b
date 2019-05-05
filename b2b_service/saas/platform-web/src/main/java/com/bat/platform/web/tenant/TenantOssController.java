package com.bat.platform.web.tenant;

import com.bat.platform.api.tenant.TenantOssServiceI;
import com.bat.platform.api.tenant.dto.TenantOssCmd;
import com.bat.platform.api.tenant.dto.TenantOssQry;
import com.bat.platform.api.tenant.dto.data.TenantOssDTO;
import com.bat.platform.web.base.BaseController;
import com.bat.platform.web.base.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "平台租户文件存储配置接口", value = "TenantOssController")
@RestController
@RequestMapping("/platform/v1/web/tenant/oss")
public class TenantOssController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(TenantOssController.class);

    @Resource
    private TenantOssServiceI service;

    @ApiOperation(value = "获取配置")
    @GetMapping(value = "/config")
    public Response<TenantOssDTO> config(@Valid TenantOssQry qry) {
        TenantOssDTO tenantOssDTO = service.config(qry);
        return Response.of(tenantOssDTO);
    }

    @ApiOperation(value = "添加配置")
    @PostMapping(value = "/add")
    public Response add(@Valid @RequestBody TenantOssCmd cmd) {
        service.add(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "更新配置")
    @PutMapping(value = "/update")
    public Response update(@Valid @RequestBody TenantOssCmd cmd) {
        service.update(cmd);
        return Response.buildSuccess();
    }
}
