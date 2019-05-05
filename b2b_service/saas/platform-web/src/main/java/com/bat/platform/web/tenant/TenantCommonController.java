package com.bat.platform.web.tenant;

import com.bat.platform.api.base.common.IdDTO;
import com.bat.platform.api.tenant.TenantCommonServiceI;
import com.bat.platform.api.tenant.dto.TenantCommonCmd;
import com.bat.platform.api.tenant.dto.data.TenantCommonDTO;
import com.bat.platform.web.base.BaseController;
import com.bat.platform.web.base.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "平台租户公共配置接口", value = "TenantCommonController")
@RestController
@RequestMapping("/platform/v1/web/tenant/common")
public class TenantCommonController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(TenantCommonController.class);

    @Resource
    private TenantCommonServiceI tenantCommonServiceI;

    @ApiOperation(value = "获取配置")
    @GetMapping(value = "/config")
    public Response<TenantCommonDTO> config(@Valid IdDTO idDTO) {
        TenantCommonDTO dto = tenantCommonServiceI.config(idDTO.getId());
        return Response.of(dto);
    }

    @ApiOperation(value = "添加配置")
    @PostMapping(value = "/add")
    public Response add(@Valid @RequestBody TenantCommonCmd cmd) {
        tenantCommonServiceI.add(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "更新配置")
    @PutMapping(value = "/update")
    public Response update(@Valid @RequestBody TenantCommonCmd cmd) {
        tenantCommonServiceI.update(cmd);
        return Response.buildSuccess();
    }
}
