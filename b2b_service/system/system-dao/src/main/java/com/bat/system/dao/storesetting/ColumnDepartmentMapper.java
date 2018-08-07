package com.bat.system.dao.storesetting;

import java.util.List;

import com.bat.system.dao.storesetting.dataobject.ColumnDepartmentDO;

public interface ColumnDepartmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ColumnDepartmentDO record);

    int insertSelective(ColumnDepartmentDO record);

    ColumnDepartmentDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ColumnDepartmentDO record);

    int updateByPrimaryKey(ColumnDepartmentDO record);

    void deleteByColumnId(Integer id);

    List<ColumnDepartmentDO> listByColumnId(Integer id);
}