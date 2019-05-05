package com.bat.thirdparty.quanyi.dao;

import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ThirdQuanyiLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ThirdQuanyiLogDO record);

    int insertSelective(ThirdQuanyiLogDO record);

    ThirdQuanyiLogDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ThirdQuanyiLogDO record);

    int updateByPrimaryKeyWithBLOBs(ThirdQuanyiLogDO record);

    int updateByPrimaryKey(ThirdQuanyiLogDO record);

    List<ThirdQuanyiLogDO> listByThirdQuanyiId(@Param("thirdQuanyiId")Integer thirdQuanyiId);
}