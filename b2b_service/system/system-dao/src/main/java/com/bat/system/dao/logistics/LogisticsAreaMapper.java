package com.bat.system.dao.logistics;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bat.system.dao.logistics.dataobject.LogisticsAreaDO;

public interface LogisticsAreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsAreaDO record);

    int insertSelective(LogisticsAreaDO record);

    LogisticsAreaDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogisticsAreaDO record);

    int updateByPrimaryKey(LogisticsAreaDO record);

    int deleteByLogisticsId(Integer id);

    List<LogisticsAreaDO> listByLogisticsId(Integer id);

    List<LogisticsAreaDO> listByLogisticsIdGroupByGroupId(Integer id);

    List<LogisticsAreaDO> listByLogisticsIdAndGroupId(@Param("logisticsId") Integer id,
        @Param("groupId") String groupId);

    void insertList(@Param("logisticsAreaDOS") List<LogisticsAreaDO> logisticsAreaDOS);

    List<LogisticsAreaDO> listByGroupId(String group);
}