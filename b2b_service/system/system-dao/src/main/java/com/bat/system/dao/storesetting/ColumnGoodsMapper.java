package com.bat.system.dao.storesetting;

import com.bat.system.dao.storesetting.dataobject.ColumnGoodsDO;

public interface ColumnGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ColumnGoodsDO record);

    int insertSelective(ColumnGoodsDO record);

    ColumnGoodsDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ColumnGoodsDO record);

    int updateByPrimaryKey(ColumnGoodsDO record);
}