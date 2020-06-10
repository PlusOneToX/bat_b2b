package com.bat.flexible.dao.index;

import com.bat.flexible.dao.index.co.DistributorBannerListCO;
import com.bat.flexible.dao.index.co.DistributorBannerPageCO;
import com.bat.flexible.dao.index.dataobject.DistributorBannerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorBannerDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorBannerDO record);

    int insertSelective(DistributorBannerDO record);

    DistributorBannerDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorBannerDO record);

    int updateByPrimaryKey(DistributorBannerDO record);

    DistributorBannerDO findMaxSortNo();

    List<DistributorBannerDO> listBySeriesId(@Param("seriesId") Integer seriesId);

    void dropStartEvent(@Param("bannerId") Integer bannerId);

    void createStartEvent(@Param("time") String time,@Param("bannerId") Integer bannerId);

    void dropEndEvent(@Param("bannerId") Integer bannerId);

    void createEndEvent(@Param("time")String time, @Param("bannerId") Integer bannerId);

    List<DistributorBannerListCO> listCOByDistributorIdsAndStatus(
            @Param("distributorIds") List<Integer> distributorIds,@Param("status") Short status);

    List<DistributorBannerPageCO> listCOByCondition(@Param("showLocation") Short showLocation, @Param("status") Short status, @Param("content") String content);
}