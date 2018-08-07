package com.bat.warehouse.manager.warehouseSetting;

import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.manager.warehouseSetting.executor.WarehouseSettingCmdExe;
import com.bat.warehouse.manager.warehouseSetting.executor.WarehouseSettingQryExe;
import com.bat.warehouse.api.base.AdminResponse;
import com.bat.warehouse.api.setting.WarehouseSettingServiceI;
import com.bat.warehouse.api.setting.dto.WarehouseSettingCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WarehouseSettingServiceImpl implements WarehouseSettingServiceI {

    @Autowired
    private WarehouseSettingCmdExe warehouseSettingCmdExe;

    @Autowired
    private WarehouseSettingQryExe warehouseSettingQryExe;
    
    @Override
    @Transactional
    public Response createWarehouseSetting(WarehouseSettingCmd warehouseSettingCmd, AdminResponse currentUser) {
        
        return warehouseSettingCmdExe.createWarehouseSetting(warehouseSettingCmd,currentUser);
    }

    @Override
    @Transactional
    public Response updateWarehouseSetting(WarehouseSettingCmd warehouseSettingCmd, AdminResponse currentUser) {
        return warehouseSettingCmdExe.updateWarehouseSetting(warehouseSettingCmd,currentUser);
    }

    @Override
    public Response listAll() {
        return warehouseSettingQryExe.listAll();
    }
}
