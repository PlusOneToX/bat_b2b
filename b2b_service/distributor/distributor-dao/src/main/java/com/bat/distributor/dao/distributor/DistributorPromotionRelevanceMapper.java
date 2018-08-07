package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorPromotionRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorPromotionRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorPromotionRelevanceDO record);

    DistributorPromotionRelevanceDO selectByPrimaryKey(Integer id);

    void deleteByDistributorId(Integer distributorId);

    List<DistributorPromotionRelevanceDO> selectAll();

    int updateByPrimaryKey(DistributorPromotionRelevanceDO record);

    List<DistributorPromotionRelevanceDO> listByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    List<DistributorPromotionRelevanceDO> listByPromotionId(String promotionId);

    DistributorPromotionRelevanceDO selectByDistributorId(Integer distributorId);

    void updateList(List<DistributorPromotionRelevanceDO> relevanceDOS);

    void insertList(List<DistributorPromotionRelevanceDO> relevanceDOS);

    int deleteByIds(@Param("ids") List<Integer> ids);
}