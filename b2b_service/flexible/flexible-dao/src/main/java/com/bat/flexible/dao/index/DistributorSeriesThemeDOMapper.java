package com.bat.flexible.dao.index;

import com.bat.flexible.dao.index.co.DistributorSeriesThemeSimpleCO;
import com.bat.flexible.dao.index.dataobject.DistributorSeriesThemeDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.dao.index.co.DistributorSeriesThemePictureCO;
import com.bat.flexible.dao.index.co.SeriesThemePageCO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorSeriesThemeDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorSeriesThemeDO record);

    int insertSelective(DistributorSeriesThemeDO record);

    DistributorSeriesThemeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorSeriesThemeDO record);

    int updateByPrimaryKey(DistributorSeriesThemeDO record);

    DistributorSeriesThemeDO findMaxSortNo();

    /**
     * 根据分销商id查询
     * @param distributorIds
     * @return
     */
    List<DistributorSeriesThemeDO> listByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    List<DistributorSeriesThemePictureCO> pageCOByDistributorIds(@Param("distributorIds")List<Integer> distributorIds,
                                                                 @Param("seriesNum") Integer seriesNum, @Param("pictureNum") Integer pictureNum);

    List<CommonPicturePageCO> listExchangeAllPictureByCondition(@Param("themeId")Integer themeId, @Param("isSanXing")Boolean isSanXing, @Param("distributorIds")List<Integer> distributorIds,
                                                                @Param("exchangeId")Integer exchangeId, @Param("materialId")Integer materialId, @Param("modelId")Integer modelId, @Param("countryId")Integer countryId);

    Integer listExchangeAllPictureByConditionCount(@Param("themeId")Integer themeId, @Param("isSanXing")Boolean isSanXing, @Param("distributorIds")List<Integer> distributorIds,
                                                                @Param("exchangeId")Integer exchangeId, @Param("materialId")Integer materialId, @Param("modelId")Integer modelId, @Param("countryId")Integer countryId);

    List<CommonPicturePageCO> listAssignExchangePictureByCondition(@Param("themeId")Integer themeId,@Param("isSanXing")Boolean isSanXing,@Param("distributorIds")List<Integer> distributorIds,
                                                         @Param("exchangeId")Integer exchangeId,@Param("materialId")Integer materialId,@Param("modelId")Integer modelId,@Param("countryId")Integer countryId);


    Integer listAssignExchangePictureByConditionCount(@Param("themeId")Integer themeId,@Param("isSanXing")Boolean isSanXing,@Param("distributorIds")List<Integer> distributorIds,
                                                                   @Param("exchangeId")Integer exchangeId,@Param("materialId")Integer materialId,@Param("modelId")Integer modelId,@Param("countryId")Integer countryId);

    List<CommonPicturePageCO> listPictureWithOutExchange(@Param("themeId")Integer themeId,@Param("isSanXing")Boolean isSanXing,@Param("distributorIds")List<Integer> distributorIds
                                                , @Param("materialId")Integer materialId,@Param("modelId") Integer modelId,@Param("countryId")Integer countryId);

    Integer listPictureWithOutExchangeCount(@Param("themeId")Integer themeId,@Param("isSanXing")Boolean isSanXing,@Param("distributorIds")List<Integer> distributorIds
            , @Param("materialId")Integer materialId,@Param("modelId") Integer modelId,@Param("countryId")Integer countryId);


    List<PictureCategoryDO> listPictureCategoryByCondtion(@Param("themeId")Integer themeId, @Param("isSanXing")Boolean isSanXing, @Param("distributorIds")List<Integer> distributorIds
            , @Param("materialId")Integer materialId, @Param("modelId") Integer modelId, @Param("countryId")Integer countryId);

    List<SeriesThemePageCO> listCOByCondition(@Param("themeId") Integer themeId,@Param("pictureCategoryId") Integer pictureCategoryId,@Param("content") String content);

    List<DistributorSeriesThemeSimpleCO> listSimpleCOByDistributorIdAndContent(@Param("distributorId") Integer distributorId, @Param("content") String content);
}