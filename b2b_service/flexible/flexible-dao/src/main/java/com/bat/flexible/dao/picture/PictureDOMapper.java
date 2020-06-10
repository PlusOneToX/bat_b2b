package com.bat.flexible.dao.picture;

import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import com.bat.flexible.dao.picture.co.DistributorPictureCO;
import com.bat.flexible.dao.picture.co.PicturePageCO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PictureDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureDO record);

    int insertSelective(PictureDO record);

    PictureDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureDO record);

    int updateByPrimaryKeyWithBLOBs(PictureDO record);

    int updateByPrimaryKey(PictureDO record);

    Integer findMaxByCategoryId(@Param("categoryId") Integer categoryId);

    List<PictureDO> listByPictureIdList(@Param("pictureIdList") List<Integer> pictureIdList);

    List<PictureDO> listByCategoryIdAndOpenFlag(@Param("categoryId") Integer categoryId, @Param("openFlag") Short openFlag);

    PictureDO findEffectByUpOrDown(@Param("categoryId") Integer categoryId, @Param("flag") Boolean flag, @Param("sequence") Integer sequence);

    void updateSequenceAddByCategoryId(@Param("categoryId") Integer categoryId, @Param("sequenceAdd") Integer sequenceAdd
            , @Param("sequenceStart") Integer sequenceStart, @Param("sequenceEnd") Integer sequenceEnd);

    List<PicturePageCO> listCOByCondition(@Param("categoryId") Integer categoryId, @Param("openFlag") Short openFlag, @Param("content") String content,
                                          @Param("type") Short type, @Param("materialIdList") List<Integer> materialIdList, @Param("modelIdList") List<Integer> modelIdList);

    List<PictureDO> listByCondition(@Param("openFlag") Short openFlag, @Param("type") Short type);

    List<PictureDO> listByDistributorIdAndPictureIdListAndCountryId(@Param("distributorId") Integer distributorId,
                                                                    @Param("pictureIdList") List<Integer> pictureIdList, @Param("countryId") Integer countryId);

    /**
     * @param isSanXing
     * @param exchangeId
     * @param materialId
     * @param distributorIds
     * @param modelId
     * @param pictureCategoryIdList
     * @param countryId
     * @param allApplicableExchangeFlag 是否全部适用图片兑换卡 true表示兑换卡全部可用 false表示指定图片 null表示非兑换卡
     * @return
     */
    List<CommonPicturePageCO> listExchangePictureByCondition(@Param("isSanXing") boolean isSanXing, @Param("exchangeId") Integer exchangeId, @Param("materialId") Integer materialId,
                                                             @Param("distributorIds") List<Integer> distributorIds, @Param("modelId") Integer modelId, @Param("pictureCategoryIdList") List<Integer> pictureCategoryIdList,
                                                             @Param("countryId") Integer countryId, @Param("allApplicableExchangeFlag") Boolean allApplicableExchangeFlag, @Param("type") Short type);

    List<DistributorPictureCO> listDistributorByCondition(@Param("distributorId") Integer distributorId, @Param("openFlag") Short openFlag,
                                                          @Param("categoryIdList") List<Integer> categoryIdList, @Param("type") Short type, @Param("isSanXing") Boolean isSanXing,
                                                          @Param("countryId") Integer countryId);

    PictureDO getByCode(@Param("code") String code);

    //通过类型条件筛选图片数据
    List<PictureDO> listByConditionType(@Param("openFlag") Short openFlag, @Param("type") Short type, @Param("typeList") List<Integer> typeList);
}