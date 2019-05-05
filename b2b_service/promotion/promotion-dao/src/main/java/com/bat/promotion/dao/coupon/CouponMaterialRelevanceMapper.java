package com.bat.promotion.dao.coupon;

import java.util.List;

import com.bat.promotion.dao.coupon.dataobject.CouponMaterialRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CouponMaterialRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponMaterialRelevanceDO record);

    int insertSelective(CouponMaterialRelevanceDO record);

    CouponMaterialRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponMaterialRelevanceDO record);

    int updateByPrimaryKey(CouponMaterialRelevanceDO record);

    void deleteByCouponId(Integer couponId);

    void createMaterialRelevanceList(List<CouponMaterialRelevanceDO> materialRelevanceDOS);

    List<CouponMaterialRelevanceDO> listMaterialRelevanceByCouponId(Integer couponId);

    List<CouponMaterialRelevanceDO> materialIdsByCouponIds(@Param("couponIds") List<Integer> couponIds);

    List<Integer> couponIdsByMaterialIds(@Param("materialIds") List<Integer> materialIds);

}