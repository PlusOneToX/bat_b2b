package com.bat.distributor.dao.platform;

import com.bat.distributor.dao.platform.dataobject.AlipayPlatformDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlipayPlatformMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AlipayPlatformDO record);

    AlipayPlatformDO selectByPrimaryKey(Integer id);

    List<AlipayPlatformDO> selectAll();

    int updateByPrimaryKey(AlipayPlatformDO record);

    AlipayPlatformDO selectByDistributorIdAndPlatformAndAppIdAndType(@Param("distributorId") Integer distributorId,
                                                         @Param("platform") String platform, @Param("appId") String appId, @Param("type") Short type);

}