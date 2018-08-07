package com.bat.distributor.dao.platform;

import com.bat.distributor.dao.platform.dataobject.DyPlatformDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DyPlatformMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DyPlatformDO record);

    int insertSelective(DyPlatformDO record);

    DyPlatformDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DyPlatformDO record);

    int updateByPrimaryKey(DyPlatformDO record);

    DyPlatformDO selectByDistributorIdAndPlatformAndAppIdAndType(@Param("distributorId") Integer distributorId,
                                                                 @Param("platform") String platform, @Param("appId") String appId, @Param("type") Short type);
}