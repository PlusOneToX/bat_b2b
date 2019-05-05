package com.bat.promotion.dao.coupon;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bat.promotion.dao.coupon.dataobject.CouponDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.promotion.dao.coupon.dataobject.CouponStatusDO;

@Mapper
public interface CouponMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponDO record);

    int insertSelective(CouponDO record);

    CouponDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponDO record);

    int updateByPrimaryKeyWithBLOBs(CouponDO record);

    int updateByPrimaryKey(CouponDO record);

    void updateCount(@Param("id") Integer id, @Param("generateCount") Integer generateCount,
        @Param("limitCount") Integer limitCount);

    List<CouponDO> listCouponByMaterial(Map map);

    List<CouponDO> listCouponByModel(Map map);

    List<CouponDO> listCouponByDistributorName(Map map);

    List<CouponDO> listCoupon(Map map);

    List<CouponDO> listCouponByIds(@Param("ids") List<Integer> ids);

    List<CouponDO> listCouponByCouponNos(@Param("couponNos") List<String> couponNos);

    void updateListCouponStatus(List<CouponStatusDO> statusDOS);

    List<CouponDO> listCouponByTime(@Param("time") Date time);

    List<Integer> couponIdsByMaterialIdsAndModelIds(Map map);

    List<Integer> couponIdsEnableByMaterialIdsAndModelIds(Map map);

}