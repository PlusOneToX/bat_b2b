package com.bat.flexible.dao.index;

import com.bat.flexible.dao.index.dataobject.BannerSeriesPictureRelevanceDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BannerSeriesPictureRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BannerSeriesPictureRelevanceDO record);

    int insertSelective(BannerSeriesPictureRelevanceDO record);

    BannerSeriesPictureRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BannerSeriesPictureRelevanceDO record);

    int updateByPrimaryKey(BannerSeriesPictureRelevanceDO record);

    List<BannerSeriesPictureRelevanceDO> listByBannerId(@Param("bannerId") Integer bannerId);

    List<Integer> listPictureIdByBannerId(@Param("bannerId")Integer bannerId);

    void deleteByBannerId(@Param("bannerId")Integer bannerId);

    List<CommonPicturePageCO> listPictureCOByBannerIdAndSeriesId(@Param("bannerId") Integer bannerId, @Param("seriesId") Integer seriesId);
}