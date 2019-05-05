package com.bat.platform.web.tenant;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.platform.web.base.BaseController;
import com.bat.platform.web.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.bat.platform.api.base.common.IdDTO;
import com.bat.platform.api.tenant.TenantUrlServiceI;
import com.bat.platform.api.tenant.dto.TenantUrlCmd;
import com.bat.platform.api.tenant.dto.TenantUrlQry;
import com.bat.platform.api.tenant.dto.data.TenantUrlDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "平台租户url配置接口", value = "TenantUrlController")
@RestController
@RequestMapping("/platform/v1/web/tenant/url")
public class TenantUrlController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(TenantUrlController.class);

    @Resource
    private TenantUrlServiceI tenantUrlServiceI;

    @ApiOperation(value = "配置列表")
    @GetMapping(value = "/list")
    public Response<List<TenantUrlDTO>> list(@Valid IdDTO idDTO) {
        List<TenantUrlDTO> list = tenantUrlServiceI.listByTenantId(idDTO.getId());
        return Response.of(list);
    }

    @ApiOperation(value = "添加配置")
    @PostMapping(value = "/add")
    public Response add(@Valid @RequestBody TenantUrlCmd cmd) {
        tenantUrlServiceI.add(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "更新配置")
    @PutMapping(value = "/update")
    public Response update(@Valid @RequestBody TenantUrlCmd cmd) {
        tenantUrlServiceI.update(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "删除配置")
    @DeleteMapping(value = "/delete")
    public Response delete(@Valid @RequestBody IdDTO idDTO) {
        tenantUrlServiceI.deleteById(idDTO.getId());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "获取租户地址")
    @GetMapping()
    public Response<TenantUrlDTO> get(@Valid TenantUrlQry qry) {
        TenantUrlDTO urlDTO = tenantUrlServiceI.get(qry);
        return Response.of(urlDTO);
    }

}
