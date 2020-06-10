package com.bat.flexible.dao.third;

import com.bat.flexible.dao.third.dataobject.ThirdSkuRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ThirdSkuRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ThirdSkuRelevanceDO record);

    int insertSelective(ThirdSkuRelevanceDO record);

    ThirdSkuRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ThirdSkuRelevanceDO record);

    int updateByPrimaryKey(ThirdSkuRelevanceDO record);

    List<ThirdSkuRelevanceDO> listByDistributorIdAndOpenFlag(@Param("distributorId") Integer distributorId,@Param("openFlag") Short openFlag);

    void deleteByDistributorId(@Param("distributorId") Integer distributorId);

    ThirdSkuRelevanceDO getByDistributorIdAndThirdSkuNo(@Param("distributorId") Integer distributorId,@Param("thirdSkuNo") String thirdSkuNo);
}