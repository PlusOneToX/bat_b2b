package com.bat.platform.web.tenant;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.platform.web.base.BaseController;
import com.bat.platform.web.base.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.bat.platform.api.base.BaseId;
import com.bat.platform.api.tenant.TenantDBServiceI;
import com.bat.platform.api.tenant.dto.TenantDBCmd;
import com.bat.platform.api.tenant.dto.TenantNoQry;
import com.bat.platform.api.tenant.dto.data.TenantDBDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "平台租户数据库接口", value = "TenantController")
@RestController
@RequestMapping("/platform/v1/web/tenant")
public class TenantDBController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(TenantDBController.class);

    @Resource
    private TenantDBServiceI service;

    @ApiOperation(value = "查询平台租户数据库列表信息")
    @GetMapping(value = "/db")
    public Response<List<TenantDBDTO>> executeDB(@Valid TenantNoQry qry) {
        List<TenantDBDTO> dtos = service.executeDB(qry);
        return Response.of(dtos);
    }

    @ApiOperation(value = "新增修改平台租户数据库信息")
    @PostMapping(value = "/db")
    public Response createUpdateDB(@RequestBody @Valid List<TenantDBCmd> cmds) {
        service.createUpdateDB(cmds);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "删除平台租户服务数据库")
    @DeleteMapping(value = "/db")
    public Response deleteDB(@RequestBody @Valid BaseId cmd) {
        service.deleteDB(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "自动生成平台租户服务数据表")
    @PostMapping(value = "/db/table")
    public Response createDBTable(@RequestBody @Valid BaseId cmd) {
        service.createDBTable(cmd);
        return Response.buildSuccess();
    }
}
