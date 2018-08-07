package com.bat.distributor.web.salesarea;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.salesarea.SalesAreaServiceI;
import com.bat.distributor.api.salesarea.dto.SalesAreaCmd;
import com.bat.distributor.api.salesarea.dto.SalesAreaId;
import com.bat.distributor.api.salesarea.dto.SalesAreaListQry;
import com.bat.distributor.api.salesarea.dto.SalesAreaOpenCmd;
import com.bat.distributor.api.salesarea.dto.data.SalesAreaDTO;
import com.bat.distributor.web.annotation.SysLog;
import com.bat.distributor.web.constants.CommonLogTypeConstantDTO;
import com.bat.distributor.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "分销商销售区域后台接口", value = "AdminSalesAreaController")
@RestController
@RequestMapping("/distributor/v1/web/admin/salesarea")
public class AdminSalesAreaController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AdminSalesAreaController.class);

    @Resource
    private SalesAreaServiceI service;

    @ApiOperation(value = "根据搜索条件查找分销商销售区域列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<SalesAreaDTO>> listSalesArea(@Valid SalesAreaListQry qry) {
        PageInfo<SalesAreaDTO> pageInfo = service.listSalesArea(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找分销商销售区域列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<SalesAreaDTO>> listSalesAreaPo(@Valid SalesAreaListQry qry) {
        PageInfo<SalesAreaDTO> pageInfo = service.listSalesArea(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据分销商销售区域id获取分销商销售区域详情")
    @GetMapping()
    public Response<SalesAreaDTO> getSalesArea(@Valid SalesAreaId qry) {
        SalesAreaDTO dto = service.getSalesArea(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminSalesArea, value = CommonLogTypeConstantDTO.AdminSalesAreaAdd)
    @ApiOperation(value = "添加分销商销售区域")
    @PostMapping()
    public Response createSalesArea(@RequestBody @Valid SalesAreaCmd cmd) {
        service.createSalesArea(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminSalesArea, value = CommonLogTypeConstantDTO.AdminSalesAreaUpdate)
    @ApiOperation(value = "更新分销商销售区域")
    @PutMapping()
    public Response updateSalesArea(@RequestBody @Valid SalesAreaCmd cmd) {
        service.updateSalesArea(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminSalesArea, value = CommonLogTypeConstantDTO.AdminSalesAreaUpdateStatus)
    @ApiOperation(value = "更新分销商销售区域状态")
    @PutMapping(value = "/open")
    public Response openSalesArea(@RequestBody @Valid SalesAreaOpenCmd cmd) {
        service.openSalesArea(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminSalesArea, value = CommonLogTypeConstantDTO.AdminSalesAreaDeleteById)
    @ApiOperation(value = "根据分销商销售区域id删除分销商销售区域")
    @DeleteMapping()
    public Response deleteSalesArea(@RequestBody @Valid SalesAreaId cmd) {
        service.deleteSalesArea(cmd);
        return Response.buildSuccess();
    }

}
