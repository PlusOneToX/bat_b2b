package com.bat.flexible.dao.index;

import com.bat.flexible.dao.index.dataobject.SeriesPictureRelevanceDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import com.bat.flexible.dao.index.co.IndexRecommendRelaCO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeriesPictureRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SeriesPictureRelevanceDO record);

    int insertSelective(SeriesPictureRelevanceDO record);

    SeriesPictureRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SeriesPictureRelevanceDO record);

    int updateByPrimaryKey(SeriesPictureRelevanceDO record);

    List<SeriesPictureRelevanceDO> listBySeriesId(@Param("seriesId") Integer seriesId);

    SeriesPictureRelevanceDO findMaxSortNo(@Param("seriesId")Integer seriesId);

    void deleteBySeriesId(@Param("seriesId")Integer seriesId);

    List<CommonPicturePageCO> listCommonPictureCOBySeriesId(@Param("seriesId") Integer seriesId);

    SeriesPictureRelevanceDO findEffect(@Param("sortNo") Integer sortNo, @Param("upFlag") Boolean upFlag,@Param("seriesId") Integer seriesId);

    List<IndexRecommendRelaCO> listCOBySeriesId(@Param("seriesId")Integer seriesId);
}