package com.bat.promotion.dao.coupon;

import java.util.List;

import com.bat.promotion.dao.coupon.dataobject.CouponModelRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CouponModelRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponModelRelevanceDO record);

    int insertSelective(CouponModelRelevanceDO record);

    CouponModelRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponModelRelevanceDO record);

    int updateByPrimaryKey(CouponModelRelevanceDO record);

    void deleteByCouponId(Integer couponId);

    void createModelRelevanceList(List<CouponModelRelevanceDO> modelRelevanceDOS);

    List<CouponModelRelevanceDO> listModelRelevanceByCouponId(Integer couponId);

    List<CouponModelRelevanceDO> modelIdsByCouponIds(@Param("couponIds") List<Integer> couponIds);

    List<Integer> couponIdsByModelIds(@Param("modelIds") List<Integer> modelIds);
}