package com.bat.warehouse.web.warehouseStockReserved;

import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.api.base.dto.WarehouseIdDTO;
import com.bat.warehouse.api.warehouseStockReserved.WarehouseStockReservedServiceI;
import com.bat.warehouse.api.warehouseStockReserved.dto.WarehouseStockReservedCmd;
import com.bat.warehouse.api.warehouseStockReserved.dto.WarehouseStockReservedPageQry;
import com.bat.warehouse.web.annotation.SysLog;
import com.bat.warehouse.web.base.BaseController;
import com.bat.warehouse.web.constants.CommonLogTypeConstantDTO;
import com.bat.warehouse.web.constants.CommonLogTypeTitleConstantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value ="/warehouse/v1/web/admin/u/p/warehouse/stock/reserved")
@Api(tags = "库存预留接口",value = "AdminWarehouseStockReservedController")
public class AdminWarehouseStockReservedController extends BaseController {

    @Autowired
    private WarehouseStockReservedServiceI warehouseStockReservedServiceI;


    @GetMapping(value = "/page")
    @ApiOperation("库存预留分页")
    public Response page(@Valid WarehouseStockReservedPageQry warehouseStockReservedPageQry){
            return warehouseStockReservedServiceI.page(warehouseStockReservedPageQry);
    }
    
    @GetMapping
    @ApiOperation(value ="查询预留详情")
    public Response detail(@ApiParam(value = "预留id",name = "id",required = true)@RequestParam(value = "id",required=false)Integer id){
       
        return warehouseStockReservedServiceI.getDetailById(id);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWarehouseStockReserved, value = CommonLogTypeConstantDTO.AdminWarehouseStockReservedAdd)
    @PostMapping
    @ApiOperation(value="新增预留")
    public Response create(@Valid @RequestBody WarehouseStockReservedCmd warehouseStockReservedCmd){
        return warehouseStockReservedServiceI.createByAdmin(warehouseStockReservedCmd,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWarehouseStockReserved, value = CommonLogTypeConstantDTO.AdminWarehouseStockReservedRelease)
    @PutMapping(value = "/release")
    @ApiOperation(value="释放预留")
    public Response release(@Valid @RequestBody WarehouseIdDTO warehouseIdDTO){
        return warehouseStockReservedServiceI.release(warehouseIdDTO.getId(),getCurrentAdmin());
    }
}
