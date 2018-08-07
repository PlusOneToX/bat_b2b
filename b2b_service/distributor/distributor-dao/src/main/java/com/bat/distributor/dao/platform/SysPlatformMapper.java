package com.bat.distributor.dao.platform;

import com.bat.distributor.dao.platform.dataobject.SysPlatformDO;
import com.bat.distributor.dao.platform.dataobject.SysPlatformListDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysPlatformMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysPlatformDO record);

    SysPlatformDO selectByPrimaryKey(Integer id);

    Integer selectByAppIdorAppKey(@Param("appId") String appId, @Param("appKey") String appKey);

    List<SysPlatformDO> selectAll();

    int updateByPrimaryKey(SysPlatformDO record);

    List<SysPlatformListDO> listSysPlatform(Map map);


    SysPlatformDO getByPlatformAndDistributorId(@Param("platform")String platform, @Param("distributorId") Integer distributorId);
}