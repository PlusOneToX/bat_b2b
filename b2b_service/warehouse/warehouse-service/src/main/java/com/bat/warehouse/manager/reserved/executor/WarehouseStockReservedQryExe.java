package com.bat.warehouse.manager.reserved.executor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.manager.common.error.WarehouseCommonErrorCode;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.api.warehouseStockReserved.dto.WarehouseStockReservedPageQry;
import com.bat.warehouse.dao.stockReserved.WarehouseStockReservedDOMapper;
import com.bat.warehouse.dao.stockReserved.dataobject.WarehouseStockReservedDO;
import com.bat.warehouse.dao.stockReservedDetail.WarehouseStockReservedDetailDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarehouseStockReservedQryExe {

    @Autowired
    private WarehouseStockReservedDetailDOMapper warehouseStockReservedDetailDOMapper;

    @Autowired
    private WarehouseStockReservedDOMapper warehouseStockReservedDOMapper;

    public Response page(WarehouseStockReservedPageQry warehouseStockReservedPageQry) {
        PageHelper.startPage(warehouseStockReservedPageQry.getPage(),warehouseStockReservedPageQry.getSize());
        List<WarehouseStockReservedDO> list = warehouseStockReservedDOMapper.listByCondition(warehouseStockReservedPageQry.getStatus(),
                warehouseStockReservedPageQry.getSource(),warehouseStockReservedPageQry.getContent());
        PageInfo<WarehouseStockReservedDO> pageInfo = new PageInfo<>(list);
        return Response.of(pageInfo);
    }

    public WarehouseStockReservedDO getById(Integer id) {
        if(id ==null){
            throw new WarehouseException(WarehouseCommonErrorCode.ID_NULL);
        }
        return warehouseStockReservedDOMapper.selectByPrimaryKey(id);
    }
}
