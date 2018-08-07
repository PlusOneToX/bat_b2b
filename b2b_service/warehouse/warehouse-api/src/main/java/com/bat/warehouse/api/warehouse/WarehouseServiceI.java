package com.bat.warehouse.api.warehouse;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.warehouse.api.base.AdminResponse;
import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.api.base.dto.WarehouseOpenFlagDTO;
import com.bat.warehouse.api.warehouse.dto.WarehouseCmd;
import com.bat.warehouse.api.warehouse.dto.WarehousePageQry;
import com.bat.warehouse.dao.warehouse.co.WarehouseCO;
import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;

public interface WarehouseServiceI {

    Response<PageInfo<WarehouseCO>> page(WarehousePageQry warehousePageQry);

    Response createWarehouse(WarehouseCmd warehouseCmd, AdminResponse currentUser);

    Response updateWarehouse(WarehouseCmd warehouseCmd, AdminResponse currentUser);

    Response delete(Integer id, AdminResponse currentUser);

    Response upOrDown(Integer id, Boolean flag, AdminResponse currentUser);

    List<WarehouseDO> listByAreaIdListAndOpenFlag(List<Integer> areaIdList, Short commonOpenFlagYes);

    WarehouseDO getByWarehouseNo(String warehouseNo);

    Response<WarehouseCO> detailById(Integer id);

    Response updateOpenFlag(WarehouseOpenFlagDTO warehouseOpenFlagDTO, AdminResponse adminResponse);

    List<WarehouseDO> listUsableWarehouse(Short syncType);

    List<WarehouseDO> listWarehouse(Short syncType, Short openFlag);

    WarehouseDO getById(Integer id);
}
