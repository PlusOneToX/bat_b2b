package com.bat.flexible.dao.index;

import com.bat.flexible.dao.index.dataobject.DistributorIndexRecommendDO;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorIndexRecommendDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorIndexRecommendDO record);

    int insertSelective(DistributorIndexRecommendDO record);

    DistributorIndexRecommendDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorIndexRecommendDO record);

    int updateByPrimaryKey(DistributorIndexRecommendDO record);

    void listCOByAllPicture();

    List<CommonPicturePageCO> listAllExchangePictureCOByCondition(@Param("isSanXing") Boolean isSanXing, @Param("countryId") Integer countryId,
                                                                  @Param("materialId") Integer materialId, @Param("modelId") Integer modelId, @Param("distributorIds") List<Integer> distributorIds,
                                                                  @Param("exchangeId") Integer exchangeId);

    List<CommonPicturePageCO> listAssginExchangePictureCO(@Param("isSanXing") Boolean isSanXing, @Param("countryId") Integer countryId,
                                     @Param("materialId") Integer materialId, @Param("modelId") Integer modelId,@Param("distributorIds") List<Integer> distributorIds,
                                     @Param("exchangeId") Integer exchangeId);

    List<CommonPicturePageCO> listPictureCOByCondition(@Param("isSanXing") Boolean isSanXing, @Param("countryId") Integer countryId,
                                  @Param("materialId") Integer materialId, @Param("modelId") Integer modelId,@Param("distributorIds") List<Integer> distributorIds);
}