package com.bat.distributor.dao.platform;

import com.bat.distributor.dao.platform.dataobject.DyPlatformDistributorDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DyPlatformDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DyPlatformDistributorDO record);

    int insertSelective(DyPlatformDistributorDO record);

    DyPlatformDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DyPlatformDistributorDO record);

    int updateByPrimaryKey(DyPlatformDistributorDO record);
}