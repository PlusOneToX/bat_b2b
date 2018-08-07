package com.bat.system.dao.storesetting;

import java.util.List;

import com.bat.system.dao.storesetting.dataobject.ColumnUserDO;

public interface ColumnUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ColumnUserDO record);

    int insertSelective(ColumnUserDO record);

    ColumnUserDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ColumnUserDO record);

    int updateByPrimaryKey(ColumnUserDO record);

    void deleteByColumnId(Integer id);

    List<ColumnUserDO> listByColumnId(Integer id);
}