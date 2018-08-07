package com.bat.warehouse.dao.stockReserved;

import com.bat.warehouse.dao.stockReserved.dataobject.WarehouseStockReservedDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface WarehouseStockReservedDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WarehouseStockReservedDO record);

    int insertSelective(WarehouseStockReservedDO record);

    WarehouseStockReservedDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarehouseStockReservedDO record);

    int updateByPrimaryKey(WarehouseStockReservedDO record);

    List<WarehouseStockReservedDO> listByCondition(@Param("status")Short status, @Param("source")Short source, @Param("content")String content);
}