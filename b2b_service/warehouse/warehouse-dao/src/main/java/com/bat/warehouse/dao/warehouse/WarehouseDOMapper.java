package com.bat.warehouse.dao.warehouse;

import java.util.List;

import com.bat.warehouse.dao.warehouse.dataobject.WarehouseDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.warehouse.dao.warehouse.co.WarehouseCO;

@Mapper
public interface WarehouseDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WarehouseDO record);

    int insertSelective(WarehouseDO record);

    WarehouseDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarehouseDO record);

    int updateByPrimaryKey(WarehouseDO record);

    Integer findMaxSortNo();

    WarehouseDO findEffect(@Param("sortNo") Integer sortNo, @Param("flag") Boolean flag);

    List<WarehouseDO> listByAreaIdListAndOpenFlagAndSyncType(@Param("areaIdList") List<Integer> areaIdList,
        @Param("openFlag") Short openFlag, @Param("syncType") Short syncType);

    WarehouseDO getByWarehouseNo(@Param("warehouseNo") String warehouseNo);

    WarehouseCO detailById(@Param("id") Integer id);

    List<WarehouseCO> listByOpenFlagAndName(@Param("openFlag") Short openFlag, @Param("content") String content);

    List<WarehouseDO> listByCondition(@Param("areaId") Integer areaId, @Param("openFlag") Short openFlag,
        @Param("warehouseNo") String warehouseNo);

    List<WarehouseDO> listAll();
}