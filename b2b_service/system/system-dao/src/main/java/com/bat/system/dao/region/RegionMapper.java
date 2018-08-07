package com.bat.system.dao.region;

import java.util.List;

import com.bat.system.dao.region.dataobject.RegionDO;
import org.apache.ibatis.annotations.Param;

public interface RegionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RegionDO record);

    int insertSelective(RegionDO record);

    RegionDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RegionDO record);

    int updateByPrimaryKey(RegionDO record);

    RegionDO getChinaRegion(Integer id);

    List<RegionDO> listByParentId(@Param("parentId") Integer parentId);

    List<RegionDO> listByParentIdAndRegionName(@Param("parentId") Integer parentId,
        @Param("regionName") String regionName);

    List<RegionDO> listRegionByLevelAndRegionName(@Param("level")Short level, @Param("regionName")  String regionName);
}