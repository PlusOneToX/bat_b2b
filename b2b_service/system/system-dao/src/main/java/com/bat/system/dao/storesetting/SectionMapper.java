package com.bat.system.dao.storesetting;

import java.util.List;
import java.util.Map;

import com.bat.system.dao.storesetting.dataobject.SectionDO;
import org.apache.ibatis.annotations.Param;

public interface SectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SectionDO record);

    int insertSelective(SectionDO record);

    SectionDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SectionDO record);

    int updateByPrimaryKey(SectionDO record);

    List<SectionDO> listByParams(@Param("params") Map<String,Object> map);

}