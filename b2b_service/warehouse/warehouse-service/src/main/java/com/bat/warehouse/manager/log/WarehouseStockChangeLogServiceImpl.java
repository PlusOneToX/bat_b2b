package com.bat.warehouse.manager.log;

import com.bat.warehouse.manager.log.executor.WarehouseStockChangeLogCmdExe;
import com.bat.warehouse.manager.log.executor.WarehouseStockChangeLogQryExe;
import com.bat.warehouse.api.log.WarehouseStockChangeLogServiceI;
import com.bat.warehouse.dao.stockChangeLog.dataobject.WarehouseStockChangeLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class WarehouseStockChangeLogServiceImpl implements WarehouseStockChangeLogServiceI {

    @Autowired
    private WarehouseStockChangeLogQryExe warehouseStockChangeLogQryExe;


    @Autowired
    private WarehouseStockChangeLogCmdExe warehouseStockChangeLogCmdExe;

    @Override
    public List<WarehouseStockChangeLogDO> listByBillNo(String billNo) {
        return warehouseStockChangeLogQryExe.listByBillNo(billNo);
    }

    @Transactional
    @Override
    public void update(WarehouseStockChangeLogDO warehouseStockChangeLogDO) {
        warehouseStockChangeLogCmdExe.update(warehouseStockChangeLogDO);
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        warehouseStockChangeLogCmdExe.deleteById(id);
    }

    @Override
    public WarehouseStockChangeLogDO create(String billNo, Short changeType, String warehouseNo, Short source, String itemCode, Integer num) {
        WarehouseStockChangeLogDO stockChangeLog = new WarehouseStockChangeLogDO();
        stockChangeLog.setBillNo(billNo);
        stockChangeLog.setChangeType(changeType);
        stockChangeLog.setWarehouseNo(warehouseNo);
        stockChangeLog.setSource(source);
        stockChangeLog.setItemCode(itemCode);
        stockChangeLog.setNum(num);
        stockChangeLog.setCreateTime(new Date());
        stockChangeLog.setUpdateTime(new Date());
        return warehouseStockChangeLogCmdExe.create(stockChangeLog);
    }


}
