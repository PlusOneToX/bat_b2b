package com.bat.distributor.dao.distributor;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bat.distributor.dao.distributor.dataobject.DistributorGoodsPromotionRelevanceDO;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorGoodsPromotionRelevanceMapper {

    List<DistributorGoodsPromotionRelevanceDO> listByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    List<DistributorGoodsPromotionRelevanceDO> listByGoodsPromotionId(@Param("id") Integer id);

    void insertList(List<DistributorGoodsPromotionRelevanceDO> addRelevanceDOS);

    void updateList(List<DistributorGoodsPromotionRelevanceDO> updateRelevanceDOS);

    void deleteByIds(List<Integer> deleteIds);

}