package com.bat.platform.web.tenant;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.platform.web.base.BaseController;
import com.bat.platform.web.base.Response;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.platform.api.base.common.IdDTO;
import com.bat.platform.api.tenant.TenantServiceI;
import com.bat.platform.api.tenant.dto.TenantCmd;
import com.bat.platform.api.tenant.dto.TenantQry;
import com.bat.platform.api.tenant.dto.data.TenantDTO;
import com.bat.platform.api.tenant.dto.data.TenantSummaryDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "平台租户接口", value = "TenantController")
@RestController
@RequestMapping("/platform/v1/web/tenant")
public class TenantController extends BaseController {

    @Resource
    private TenantServiceI service;

    @ApiOperation(value = "租户列表")
    @GetMapping(value = "/list")
    public Response<PageInfo<TenantDTO>> list(@Valid TenantQry qry) {
        PageInfo<TenantDTO> pageInfo = service.list(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "租户详情")
    @GetMapping(value = "/detail")
    public Response<TenantDTO> detail(@Valid IdDTO idDTO) {
        TenantDTO tenantDTO = service.detail(idDTO.getId());
        return Response.of(tenantDTO);
    }

    @ApiOperation(value = "租户添加")
    @PostMapping(value = "/add")
    public Response add(@Valid @RequestBody TenantCmd cmd) {
        service.add(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "租户更新")
    @PutMapping(value = "/update")
    public Response update(@Valid @RequestBody TenantCmd cmd) {
        service.update(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "租户删除")
    @DeleteMapping(value = "/delete")
    public Response delete(@Valid @RequestBody IdDTO idDTO) {
        service.deleteById(idDTO.getId());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "汇总")
    @GetMapping(value = "/summary")
    public Response<TenantSummaryDTO> summary(@Valid IdDTO idDTO) {
        TenantSummaryDTO tenantSummaryDTO = service.summary(idDTO.getId());
        return Response.of(tenantSummaryDTO);
    }
}
