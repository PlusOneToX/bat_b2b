package com.bat.warehouse.manager.reserved.executor;

import com.bat.warehouse.dao.stockReservedDetail.WarehouseStockReservedDetailDOMapper;
import com.bat.warehouse.dao.stockReservedDetail.dataobject.WarehouseStockReservedDetailDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarehouseStockReservedDetailQryExe {

    @Autowired
    private WarehouseStockReservedDetailDOMapper warehouseStockReservedDetailDOMapper;


    public List<WarehouseStockReservedDetailDO> listByReservedId(Integer reservedId) {
        return warehouseStockReservedDetailDOMapper.listByReservedId(reservedId);
    }
}
