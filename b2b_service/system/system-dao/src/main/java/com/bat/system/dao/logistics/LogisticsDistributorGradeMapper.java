package com.bat.system.dao.logistics;

import java.util.List;

import com.bat.system.dao.logistics.dataobject.LogisticsDistributorGradeDO;

public interface LogisticsDistributorGradeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsDistributorGradeDO record);

    int insertSelective(LogisticsDistributorGradeDO record);

    LogisticsDistributorGradeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogisticsDistributorGradeDO record);

    int updateByPrimaryKey(LogisticsDistributorGradeDO record);

    List<LogisticsDistributorGradeDO> listByLogisticsId(Integer id);

    void deleteByLogisticsId(Integer id);
}