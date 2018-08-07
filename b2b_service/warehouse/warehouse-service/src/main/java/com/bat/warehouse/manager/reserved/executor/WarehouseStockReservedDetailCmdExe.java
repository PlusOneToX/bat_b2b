package com.bat.warehouse.manager.reserved.executor;

import com.bat.warehouse.dao.stockReservedDetail.WarehouseStockReservedDetailDOMapper;
import com.bat.warehouse.dao.stockReservedDetail.dataobject.WarehouseStockReservedDetailDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarehouseStockReservedDetailCmdExe {

    @Autowired
    private WarehouseStockReservedDetailDOMapper warehouseStockReservedDetailDOMapper;


    public List<WarehouseStockReservedDetailDO> listByReservedId(Integer reservedId) {
        return warehouseStockReservedDetailDOMapper.listByReservedId(reservedId);
    }

    public void update(WarehouseStockReservedDetailDO reservedDetailDO) {
        warehouseStockReservedDetailDOMapper.updateByPrimaryKey(reservedDetailDO);
    }

    public void deleteById(Integer id) {
        warehouseStockReservedDetailDOMapper.deleteByPrimaryKey(id);
    }

    public void create(WarehouseStockReservedDetailDO detailDO) {
        warehouseStockReservedDetailDOMapper.insert(detailDO);
    }
}
