package com.bat.flexible.dao.index;

import com.bat.flexible.dao.index.dataobject.DistributorSeriesThemeRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DistributorSeriesThemeRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorSeriesThemeRelevanceDO record);

    int insertSelective(DistributorSeriesThemeRelevanceDO record);

    DistributorSeriesThemeRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorSeriesThemeRelevanceDO record);

    int updateByPrimaryKey(DistributorSeriesThemeRelevanceDO record);
    List<DistributorSeriesThemeRelevanceDO> listBySeriesThemeId(@Param("seriesThemeId") Integer seriesThemeId);

    void deleteBySeriesThemeId(@Param("seriesThemeId")Integer seriesThemeId);

    List<DistributorSeriesThemeRelevanceDO> listByDistributorIdList(@Param("distributorIdList") List<Integer> distributorIdList,
                                                                    @Param("seriesThemeId") Integer seriesThemeId);
}