package com.bat.warehouse.manager.warehouseSetting.executor;

import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.dao.warehouseSetting.WarehouseSettingDOMapper;
import com.bat.warehouse.dao.warehouseSetting.dataobject.WarehouseSettingDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarehouseSettingQryExe {

    @Autowired
    private WarehouseSettingDOMapper warehouseSettingDOMapper;

    public Response listAll() {
        List<WarehouseSettingDO> list = warehouseSettingDOMapper.listAll();
        return Response.of(list);
    }
}
