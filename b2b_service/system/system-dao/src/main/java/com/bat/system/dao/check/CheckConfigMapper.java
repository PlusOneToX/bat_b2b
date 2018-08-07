package com.bat.system.dao.check;

import java.util.List;
import java.util.Map;

import com.bat.system.dao.check.dataobject.CheckConfigDO;
import org.apache.ibatis.annotations.Param;

public interface CheckConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CheckConfigDO record);

    int insertSelective(CheckConfigDO record);

    CheckConfigDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CheckConfigDO record);

    int updateByPrimaryKey(CheckConfigDO record);

    List<CheckConfigDO> listAll(@Param("ext") Short ext);

    List<CheckConfigDO> getConfigsByExt(Short ext);

    List<CheckConfigDO> selectByParams(@Param("params") Map<String, Object> map);
}