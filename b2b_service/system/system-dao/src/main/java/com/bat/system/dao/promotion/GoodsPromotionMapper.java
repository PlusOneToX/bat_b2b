package com.bat.system.dao.promotion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bat.system.dao.promotion.dataobject.DistributorGoodsPromotionRelevanceDO;
import com.bat.system.dao.promotion.dataobject.GoodsPromotionDO;

public interface GoodsPromotionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsPromotionDO record);

    int insertSelective(GoodsPromotionDO record);

    GoodsPromotionDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsPromotionDO record);

    int updateByPrimaryKey(GoodsPromotionDO record);

    List<GoodsPromotionDO> goodsPromotionList(@Param("start") Integer start, @Param("count") Integer count);

    Integer addGoodsPromotion(GoodsPromotionDO goodsPromotionDO);

    List<GoodsPromotionDO> queryGoodsPromotionDescTwo();

    Integer createGoodsScalePriceRelevanceList(@Param("id") Integer id,
        @Param("scalePriceIds") List<Integer> scalePriceIds);

    Integer createGoodsDistributorRelevanceList(@Param("id") Integer id,
        @Param("distributorIds") List<Integer> distributorIds);

    Integer createGoodsDepartmentRelevanceList(@Param("id") Integer id,
        @Param("departmentIds") List<Integer> departmentIds);

    Integer createGoodsAdminRelevanceList(@Param("id") Integer id, @Param("adminIds") List<Integer> adminIds);

    GoodsPromotionDO getGoodsPromotion(@Param("id") Integer id);

    List<Integer> listGoodsScalePriceRelevanceId(@Param("goodsId") Integer goodsId);

    List<Integer> listGoodsDistributorRelevanceId(@Param("goodsId") Integer goodsId);

    List<Integer> listGoodsDepartmentRelevanceId(@Param("goodsId") Integer goodsId);

    List<Integer> listGoodsAdminRelevanceId(@Param("goodsId") Integer goodsId);

    List<DistributorGoodsPromotionRelevanceDO> promotionsquery(@Param("userId") Integer userId);

    List<GoodsPromotionDO> getGoodsPromotionDesc();

    List<DistributorGoodsPromotionRelevanceDO>
        listByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    List<DistributorGoodsPromotionRelevanceDO> listByGoodsPromotionId(@Param("id") String id);

    void insertList(List<DistributorGoodsPromotionRelevanceDO> addRelevanceDOS);

    void updateList(List<DistributorGoodsPromotionRelevanceDO> updateRelevanceDOS);

    void deleteByIds(@Param("ids") List<Integer> deleteIds);

    Integer updateGoodsPromotion(GoodsPromotionDO goodsPromotionDO);

    Integer deleteGoodsScalePriceRelevanceList(@Param("id") Integer id);

    Integer deleteGoodsDistributorRelevanceList(@Param("id") Integer id);

    Integer deleteGoodsDepartmentRelevanceList(@Param("id") Integer id);

    Integer deleteGoodsAdminRelevanceList(@Param("id") Integer id);

    Integer deleteGoodsPromotionRelevanceById(@Param("id") String id);

    void deleteGoodsPromotionById(@Param("id") Integer id);

    void invalidGoodsPromotion(@Param("id") Integer id);

    void deleteRelevance(@Param("id") String id);

    List<GoodsPromotionDO>
        getGoodsPromotionListByIds(@Param("goodsPromotionIdList") List<Integer> goodsPromotionIdList);
}