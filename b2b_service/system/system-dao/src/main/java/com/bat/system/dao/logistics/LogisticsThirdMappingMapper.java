package com.bat.system.dao.logistics;

import org.apache.ibatis.annotations.Param;

import com.bat.system.dao.logistics.dataobject.LogisticsThirdMappingDO;

public interface LogisticsThirdMappingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsThirdMappingDO record);

    int insertSelective(LogisticsThirdMappingDO record);

    LogisticsThirdMappingDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogisticsThirdMappingDO record);

    int updateByPrimaryKey(LogisticsThirdMappingDO record);

    LogisticsThirdMappingDO getThirdLogisticsByThirdTypeAndLogisticsId(@Param("thirdType") Short thirdType,
        @Param("logisticsId") Integer logisticsId);
}