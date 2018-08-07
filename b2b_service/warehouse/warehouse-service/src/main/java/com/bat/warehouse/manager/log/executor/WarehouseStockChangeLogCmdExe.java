package com.bat.warehouse.manager.log.executor;

import com.bat.warehouse.manager.common.error.WarehouseCommonErrorCode;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.dao.stockChangeLog.WarehouseStockChangeLogDOMapper;
import com.bat.warehouse.dao.stockChangeLog.dataobject.WarehouseStockChangeLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WarehouseStockChangeLogCmdExe {

    @Autowired
    private WarehouseStockChangeLogDOMapper warehouseStockChangeLogDOMapper;


    public void update(WarehouseStockChangeLogDO warehouseStockChangeLogDO) {
        warehouseStockChangeLogDOMapper.updateByPrimaryKey(warehouseStockChangeLogDO);
    }

    public void deleteById(Integer id) {
        if(id==null){
            throw new WarehouseException(WarehouseCommonErrorCode.ID_NULL);
        }
        warehouseStockChangeLogDOMapper.deleteByPrimaryKey(id);
    }

    public WarehouseStockChangeLogDO create(WarehouseStockChangeLogDO stockChangeLog) {
         warehouseStockChangeLogDOMapper.insert(stockChangeLog);
        return stockChangeLog;
    }
}
