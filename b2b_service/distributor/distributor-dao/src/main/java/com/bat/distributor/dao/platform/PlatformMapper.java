package com.bat.distributor.dao.platform;

import com.bat.distributor.dao.platform.dataobject.PlatformDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PlatformMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformDO record);

    PlatformDO selectByPrimaryKey(Integer id);

    PlatformDO selectByPlatformNo(@Param("platformNo") String platformNo);

    List<PlatformDO> listByPlatformNos(@Param("platformNos") List<String> platformNos);

    List<PlatformDO> selectAll();

    int updateByPrimaryKey(PlatformDO record);

    void openPlatform(@Param("id") Integer id, @Param("openFlag") Short openFlag);

    List<PlatformDO> listPlatform(Map map);

    List<PlatformDO> listByOpenFlag(@Param("openFlag")Short openFlag);

    List<PlatformDO> listByOpenFlagAndNameLike(@Param("openFlag")Short openFlag,@Param("name") String name);
}