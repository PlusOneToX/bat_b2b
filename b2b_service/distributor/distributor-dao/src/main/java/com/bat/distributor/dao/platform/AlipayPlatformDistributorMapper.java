package com.bat.distributor.dao.platform;

import com.bat.distributor.dao.platform.dataobject.AlipayPlatformDistributorDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlipayPlatformDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AlipayPlatformDistributorDO record);

    AlipayPlatformDistributorDO selectByPrimaryKey(Integer id);

    List<AlipayPlatformDistributorDO> selectAll();

    int updateByPrimaryKey(AlipayPlatformDistributorDO record);
}