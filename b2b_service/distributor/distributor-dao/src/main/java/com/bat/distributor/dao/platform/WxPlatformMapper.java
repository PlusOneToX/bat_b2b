package com.bat.distributor.dao.platform;

import com.bat.distributor.dao.platform.dataobject.WxPlatformDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WxPlatformMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxPlatformDO record);

    WxPlatformDO selectByPrimaryKey(Integer id);

    List<WxPlatformDO> selectAll();

    int updateByPrimaryKey(WxPlatformDO record);

    List<WxPlatformDO> listWxPlatform(Map map);

    WxPlatformDO selectByDistributorIdAndPlatformAndAppIdAndType(@Param("distributorId") Integer distributorId,
        @Param("platform") String platform, @Param("appId") String appId, @Param("type") Short type);
    WxPlatformDO getByAppId(@Param("appId") String appId);
    List<WxPlatformDO> listWxPlatformByDistributorId(@Param("distributorId") Integer distributorId,
        @Param("type") Short type);

    List<WxPlatformDO> listDistributorWxPlatform(@Param("distributorId") Integer distributorId,@Param("appId") String appId, @Param("type") Short type);
}