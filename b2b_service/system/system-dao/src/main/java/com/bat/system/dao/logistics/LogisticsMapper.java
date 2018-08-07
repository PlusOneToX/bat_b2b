package com.bat.system.dao.logistics;

import java.util.List;
import java.util.Map;

import com.bat.system.dao.logistics.dataobject.LogisticsDO;
import com.bat.system.dao.logistics.dataobject.LogisticsDOExtend;

public interface LogisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsDO record);

    int insertSelective(LogisticsDO record);

    LogisticsDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogisticsDO record);

    int updateByPrimaryKey(LogisticsDO record);

    // List<LogisticsDO> listByName(String name);

    List<LogisticsDOExtend> selectExtendByParams(Map<String, Object> map);

    // List<LogisticsDO> listByManufactor(@Param("manufactors") String manufactors);

    // List<LogisticsDO> listByKDNCodeAndDistributorId(@Param("code") String code,
    // @Param("distributorId") Integer distributorId);

    // List<LogisticsDO> listLogisticsByName(String name);

    // List<LogisticsDO> listLogisticsByLogisticsErpId(String logisticsErpId);
}