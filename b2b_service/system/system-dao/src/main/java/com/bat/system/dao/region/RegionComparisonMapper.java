package com.bat.system.dao.region;

import java.util.List;

import com.bat.system.dao.region.dataobject.RegionComparisonDO;
import org.apache.ibatis.annotations.Param;

public interface RegionComparisonMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RegionComparisonDO record);

    int insertSelective(RegionComparisonDO record);

    RegionComparisonDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RegionComparisonDO record);

    int updateByPrimaryKey(RegionComparisonDO record);

    List<RegionComparisonDO> listRegionByParentIdAndAnotherName(@Param("parentId") Integer parentId,
        @Param("anotherName") String anotherName);
}