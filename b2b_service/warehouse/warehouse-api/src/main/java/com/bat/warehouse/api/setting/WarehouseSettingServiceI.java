package com.bat.warehouse.api.setting;

import com.bat.warehouse.api.base.AdminResponse;
import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.api.setting.dto.WarehouseSettingCmd;

public interface WarehouseSettingServiceI {
    Response createWarehouseSetting(WarehouseSettingCmd warehouseSettingCmd, AdminResponse currentUser);

    Response updateWarehouseSetting(WarehouseSettingCmd warehouseSettingCmd, AdminResponse currentUser);

    Response listAll();
}
