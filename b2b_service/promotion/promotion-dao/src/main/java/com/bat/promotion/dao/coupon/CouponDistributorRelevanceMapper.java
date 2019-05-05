package com.bat.promotion.dao.coupon;

import java.util.List;

import com.bat.promotion.dao.coupon.dataobject.CouponDistributorRelevanceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponDistributorRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponDistributorRelevanceDO record);

    int insertSelective(CouponDistributorRelevanceDO record);

    CouponDistributorRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponDistributorRelevanceDO record);

    int updateByPrimaryKey(CouponDistributorRelevanceDO record);

    void deleteByCouponId(Integer couponId);

    void createDistributorRelevanceList(List<CouponDistributorRelevanceDO> distributorRelevanceDOS);

    List<CouponDistributorRelevanceDO> listDistributorRelevanceByCouponId(Integer couponId);
}