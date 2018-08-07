package com.bat.warehouse.dao.stockReservedDetail;


import com.bat.warehouse.dao.stockReservedDetail.dataobject.WarehouseStockReservedDetailDO;
import com.bat.warehouse.dao.stockChangeLog.dataobject.WarehouseStockChangeLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface WarehouseStockReservedDetailDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WarehouseStockReservedDetailDO record);

    int insertSelective(WarehouseStockReservedDetailDO record);

    WarehouseStockReservedDetailDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarehouseStockReservedDetailDO record);

    int updateByPrimaryKey(WarehouseStockReservedDetailDO record);

    List<WarehouseStockReservedDetailDO> listByReservedId(@Param("reservedId") Integer reservedId);

    List<WarehouseStockChangeLogDO> listByBillNo(@Param("billNo") String billNo);
}