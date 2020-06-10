package com.bat.flexible.dao.index;

import com.bat.flexible.dao.index.dataobject.DistributorBannerRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorBannerRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorBannerRelevanceDO record);

    int insertSelective(DistributorBannerRelevanceDO record);

    DistributorBannerRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorBannerRelevanceDO record);

    int updateByPrimaryKey(DistributorBannerRelevanceDO record);

    List<DistributorBannerRelevanceDO> listByBannerId(@Param("bannerId") Integer bannerId);

    void deleteByBannerIdList(@Param("bannerIdList") List<Integer> bannerIdList);
}