package com.bat.warehouse.dao.stockChangeLog;


import com.bat.warehouse.dao.stockChangeLog.dataobject.WarehouseStockChangeLogDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WarehouseStockChangeLogDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WarehouseStockChangeLogDO record);

    int insertSelective(WarehouseStockChangeLogDO record);

    WarehouseStockChangeLogDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarehouseStockChangeLogDO record);

    int updateByPrimaryKey(WarehouseStockChangeLogDO record);
}