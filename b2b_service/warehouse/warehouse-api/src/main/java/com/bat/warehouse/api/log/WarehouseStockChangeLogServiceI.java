package com.bat.warehouse.api.log;

import com.bat.warehouse.dao.stockChangeLog.dataobject.WarehouseStockChangeLogDO;

import java.util.List;

public interface WarehouseStockChangeLogServiceI {
    /**
     * 根据流水号查询库存变更记录列表
     * @param billNo
     * @return
     */
    List<WarehouseStockChangeLogDO> listByBillNo(String billNo);

    /**
     * 修改
     * @param warehouseStockChangeLogDO
     */
    void update(WarehouseStockChangeLogDO warehouseStockChangeLogDO);

    void deleteById(Integer id);

    WarehouseStockChangeLogDO create(String billNo, Short changeType, String warehouseNo, Short source, String itemNo, Integer num);
}
