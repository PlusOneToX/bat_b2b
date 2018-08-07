package com.bat.system.dao.storesetting;

import java.util.List;

import com.bat.system.dao.storesetting.dataobject.ColumnDistributorGradeDO;

public interface ColumnDistributorGradeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ColumnDistributorGradeDO record);

    int insertSelective(ColumnDistributorGradeDO record);

    ColumnDistributorGradeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ColumnDistributorGradeDO record);

    int updateByPrimaryKey(ColumnDistributorGradeDO record);

    void deleteByColumnId(Integer id);

    List<ColumnDistributorGradeDO> listByColumnId(Integer id);
}