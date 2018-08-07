package com.bat.warehouse.api.warehouseStockReserved;

import com.bat.warehouse.api.base.AdminResponse;
import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.api.warehouseStockReserved.dto.WarehouseStockReservedCmd;
import com.bat.warehouse.api.warehouseStockReserved.dto.WarehouseStockReservedPageQry;

public interface WarehouseStockReservedServiceI {
    Response page(WarehouseStockReservedPageQry warehouseStockReservedPageQry);

    Response getDetailById(Integer id);

    Response createByAdmin(WarehouseStockReservedCmd warehouseStockReservedCmd, AdminResponse currentAdmin);

    /**
     * 释放预留
     * @param id
     * @param currentAdmin
     * @return
     */
    Response release(Integer id, AdminResponse currentAdmin);
}
