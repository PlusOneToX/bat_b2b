package com.bat.warehouse.web.warehouseSetting;

import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.web.annotation.SysLog;
import com.bat.warehouse.web.constants.CommonLogTypeConstantDTO;
import com.bat.warehouse.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.warehouse.api.setting.WarehouseSettingServiceI;
import com.bat.warehouse.api.setting.dto.WarehouseSettingCmd;
import com.bat.warehouse.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@Api(tags = "仓库设置后台接口",value ="AdminWarehouseSettingController" )
@RequestMapping("/warehouse/v1/web/admin/u/p/warehouseSetting")
public class AdminWarehouseSettingController extends BaseController {

    @Autowired
    private WarehouseSettingServiceI warehouseSettingServiceI;



    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWarehouseSetting, value = CommonLogTypeConstantDTO.AdminWarehouseSettingAdd)
    @PostMapping
    @ApiOperation(value = "新增仓库设置")
    public Response createWarehouse(@Valid @RequestBody WarehouseSettingCmd warehouseSettingCmd){
        return warehouseSettingServiceI.createWarehouseSetting(warehouseSettingCmd,getCurrentUser());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWarehouseSetting, value = CommonLogTypeConstantDTO.AdminWarehouseSettingUpdate)
    @PutMapping
    @ApiOperation(value = "修改仓库设置")
    public Response updateWarehouse( @Valid  @RequestBody WarehouseSettingCmd warehouseSettingCmd){
        return warehouseSettingServiceI.updateWarehouseSetting(warehouseSettingCmd,getCurrentUser());
    }

    @GetMapping(value = "/list")
    @ApiOperation(value ="仓库设置列表")
    public Response list(){
        return warehouseSettingServiceI.listAll();
    }


}
