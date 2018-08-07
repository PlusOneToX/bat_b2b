package com.bat.system.dao.storesetting;

import java.util.List;

import com.bat.system.dao.storesetting.dataobject.ColumnDistributorDO;

public interface ColumnDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ColumnDistributorDO record);

    int insertSelective(ColumnDistributorDO record);

    ColumnDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ColumnDistributorDO record);

    int updateByPrimaryKey(ColumnDistributorDO record);

    void deleteByColumnId(Integer id);

    List<ColumnDistributorDO> listByColumnId(Integer id);
}