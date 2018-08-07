package com.bat.warehouse.dao.warehouseSetting;


import com.bat.warehouse.dao.warehouseSetting.dataobject.WarehouseSettingDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WarehouseSettingDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WarehouseSettingDO record);

    int insertSelective(WarehouseSettingDO record);

    WarehouseSettingDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarehouseSettingDO record);

    int updateByPrimaryKey(WarehouseSettingDO record);

    WarehouseSettingDO findByType(@Param("type") Short type);

    List<WarehouseSettingDO> listAll();
}