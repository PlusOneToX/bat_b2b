package com.bat.system.dao.logistics;

import java.util.List;

import com.bat.system.dao.logistics.dataobject.LogisticsDistributorDO;

public interface LogisticsDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsDistributorDO record);

    int insertSelective(LogisticsDistributorDO record);

    LogisticsDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogisticsDistributorDO record);

    int updateByPrimaryKey(LogisticsDistributorDO record);

    List<LogisticsDistributorDO> listByLogisticsId(Integer id);

    void deleteByLogisticsId(Integer id);
}