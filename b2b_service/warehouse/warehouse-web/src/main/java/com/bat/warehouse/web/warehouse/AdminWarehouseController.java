package com.bat.warehouse.web.warehouse;

import com.github.pagehelper.PageInfo;
import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.api.base.dto.WarehouseIdDTO;
import com.bat.warehouse.api.base.dto.WarehouseOpenFlagDTO;
import com.bat.warehouse.api.warehouse.WarehouseServiceI;
import com.bat.warehouse.api.warehouse.dto.WarehouseCmd;
import com.bat.warehouse.api.warehouse.dto.WarehouseUpDownDTO;
import com.bat.warehouse.web.annotation.SysLog;
import com.bat.warehouse.web.base.BaseController;
import com.bat.warehouse.web.constants.CommonLogTypeConstantDTO;
import com.bat.warehouse.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.warehouse.api.warehouse.dto.WarehousePageQry;
import com.bat.warehouse.dao.warehouse.co.WarehouseCO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@Api(tags = "仓库后台接口",value ="AdminWarehouseController" )
@RequestMapping("/warehouse/v1/web/admin/u")
public class AdminWarehouseController  extends BaseController {

    @Autowired
    private WarehouseServiceI warehouseServiceI;


    @GetMapping(value = {"/p/warehouse/page","/po/warehouse/page"})
    @ApiOperation(value = "分页查询仓库列表")
    public Response<PageInfo<WarehouseCO>> page(@Valid WarehousePageQry warehousePageQry){
        return warehouseServiceI.page(warehousePageQry);
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWarehouse, value = CommonLogTypeConstantDTO.AdminWarehouseAdd)
    @PostMapping(value = "/p/warehouse")
    @ApiOperation(value = "新增仓库")
    public Response createWarehouse(@Valid  @RequestBody WarehouseCmd warehouseCmd){
        return warehouseServiceI.createWarehouse(warehouseCmd,getCurrentUser());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWarehouse, value = CommonLogTypeConstantDTO.AdminWarehouseUpdate)
    @PutMapping(value = "/p/warehouse")
    @ApiOperation(value = "修改仓库")
    public Response updateWarehouse(@Valid @RequestBody WarehouseCmd warehouseCmd){
        return warehouseServiceI.updateWarehouse(warehouseCmd,getCurrentUser());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWarehouse, value = CommonLogTypeConstantDTO.AdminWarehouseDelete)
    @DeleteMapping(value = "/p/warehouse")
    @ApiOperation(value = "删除仓库")
    public Response delete(@Valid @RequestBody WarehouseIdDTO warehouseIdDTO){

        return warehouseServiceI.delete(warehouseIdDTO.getId(),getCurrentUser());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWarehouse, value = CommonLogTypeConstantDTO.AdminWarehouseUpOrDown)
    @PutMapping("/p/warehouse/upOrDown")
    @ApiOperation(value = "仓库上下移动")
    public Response upOrDown(@Valid @RequestBody WarehouseUpDownDTO warehouseUpDownDTO
                             ){
        return warehouseServiceI.upOrDown(warehouseUpDownDTO.getId(),warehouseUpDownDTO.getFlag(),getCurrentUser());
    }

    @GetMapping(value = "/p/warehouse")
    @ApiOperation(value = "仓库详情")
    public Response<WarehouseCO> detail(@ApiParam(value = "主键id",name = "id",required = false)@RequestParam(value = "id",required = false)Integer id){

        return warehouseServiceI.detailById(id);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWarehouse, value = CommonLogTypeConstantDTO.AdminWarehouseUpdateStatus)
    @PutMapping("/p/warehouse/updateStatus")
    @ApiOperation(value = "启用禁用仓库")
    public Response updateStatus(@Valid @RequestBody WarehouseOpenFlagDTO warehouseOpenFlagDTO){
        return warehouseServiceI.updateOpenFlag(warehouseOpenFlagDTO,getCurrentAdmin());
    }
}
