package com.bat.warehouse.manager.log.executor;

import com.bat.warehouse.dao.stockChangeLog.dataobject.WarehouseStockChangeLogDO;
import com.bat.warehouse.dao.stockReservedDetail.WarehouseStockReservedDetailDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarehouseStockChangeLogQryExe {

    @Autowired
    private WarehouseStockReservedDetailDOMapper warehouseStockReservedDetailDOMapper;

    public List<WarehouseStockChangeLogDO> listByBillNo(String billNo) {
        return warehouseStockReservedDetailDOMapper.listByBillNo(billNo);
    }
}
