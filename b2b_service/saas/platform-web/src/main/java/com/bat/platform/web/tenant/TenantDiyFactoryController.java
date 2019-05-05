package com.bat.platform.web.tenant;

import com.bat.platform.api.base.common.IdDTO;
import com.bat.platform.api.tenant.TenantDiyFactoryServiceI;
import com.bat.platform.api.tenant.dto.TenantDiyFactoryCmd;
import com.bat.platform.api.tenant.dto.data.TenantDiyFactoryDTO;
import com.bat.platform.web.base.BaseController;
import com.bat.platform.web.base.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "平台租户定制工厂配置接口", value = "TenantDiyFactoryController")
@RestController
@RequestMapping("/platform/v1/web/tenant/diyFactory")
public class TenantDiyFactoryController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(TenantDiyFactoryController.class);

    @Resource
    private TenantDiyFactoryServiceI tenantDiyFactoryServiceI;

    @ApiOperation(value = "获取列表")
    @GetMapping(value = "/list")
    public Response<List<TenantDiyFactoryDTO>> list(@Valid IdDTO idDTO) {
        List<TenantDiyFactoryDTO> list= tenantDiyFactoryServiceI.listByTenantId(idDTO.getId());
        return Response.of(list);
    }

    @ApiOperation(value = "添加配置")
    @PostMapping(value = "/add")
    public Response add(@Valid @RequestBody TenantDiyFactoryCmd cmd) {
        tenantDiyFactoryServiceI.add(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "更新配置")
    @PutMapping(value = "/update")
    public Response update(@Valid @RequestBody TenantDiyFactoryCmd cmd) {
        tenantDiyFactoryServiceI.update(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping(value = "/delete")
    public Response delete(@Valid @RequestBody IdDTO idDTO) {
        tenantDiyFactoryServiceI.deleteById(idDTO.getId());
        return Response.buildSuccess();
    }
}
