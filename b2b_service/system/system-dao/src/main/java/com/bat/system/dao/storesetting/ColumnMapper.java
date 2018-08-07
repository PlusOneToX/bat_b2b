package com.bat.system.dao.storesetting;

import java.util.List;
import java.util.Map;

import com.bat.system.dao.storesetting.dataobject.ColumnDO;
import org.apache.ibatis.annotations.Param;

public interface ColumnMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ColumnDO record);

    int insertSelective(ColumnDO record);

    ColumnDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ColumnDO record);

    int updateByPrimaryKey(ColumnDO record);

    List<ColumnDO> listByParams(@Param("params") Map<String, Object> map);
}