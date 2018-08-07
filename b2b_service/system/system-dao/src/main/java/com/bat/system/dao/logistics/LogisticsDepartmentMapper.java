package com.bat.system.dao.logistics;

import java.util.List;

import com.bat.system.dao.logistics.dataobject.LogisticsDepartmentDO;

public interface LogisticsDepartmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsDepartmentDO record);

    int insertSelective(LogisticsDepartmentDO record);

    LogisticsDepartmentDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogisticsDepartmentDO record);

    int updateByPrimaryKey(LogisticsDepartmentDO record);

    List<LogisticsDepartmentDO> listByLogisticsId(Integer id);

    void deleteByLogisticsId(Integer id);
}