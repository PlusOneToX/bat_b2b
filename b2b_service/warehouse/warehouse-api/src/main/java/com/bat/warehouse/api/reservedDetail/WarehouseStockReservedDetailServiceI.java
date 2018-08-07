package com.bat.warehouse.api.reservedDetail;

import com.bat.warehouse.api.base.AdminResponse;
import com.bat.warehouse.api.warehouseStockReserved.dto.WarehouseStockReservedDetailCmd;
import com.bat.warehouse.dao.stockReservedDetail.dataobject.WarehouseStockReservedDetailDO;

import java.util.List;

public interface WarehouseStockReservedDetailServiceI {
    List<WarehouseStockReservedDetailDO> listByReservedId(Integer resveredId);

    /**
     * 保存关联关系
     * @param detailCmdList
     * @param currentAdmin
     * @param source
     * @param reservedId
     */
    List<WarehouseStockReservedDetailDO>  save(List<WarehouseStockReservedDetailCmd> detailCmdList, AdminResponse currentAdmin, Short source, Integer reservedId);
}
