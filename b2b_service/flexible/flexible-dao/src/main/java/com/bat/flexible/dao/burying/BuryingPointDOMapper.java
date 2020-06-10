package com.bat.flexible.dao.burying;

import com.bat.flexible.dao.burying.dataobject.BuryingPointDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BuryingPointDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BuryingPointDO record);

    int insertSelective(BuryingPointDO record);

    BuryingPointDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BuryingPointDO record);

    int updateByPrimaryKey(BuryingPointDO record);
}