package com.bat.promotion.dao.coupon;

import java.util.List;
import java.util.Map;

import com.bat.promotion.dao.coupon.dataobject.CouponCustomerDO;
import com.bat.promotion.dao.coupon.dataobject.CouponStatusDO;
import com.bat.promotion.dao.coupon.dataobject.UserCustomerCouponDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CouponCustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponCustomerDO record);

    int insertList(List<CouponCustomerDO> records);

    int insertSelective(CouponCustomerDO record);

    CouponCustomerDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponCustomerDO record);

    int updateByPrimaryKey(CouponCustomerDO record);

    void updateListCouponStatus(List<CouponStatusDO> statusDOS);

    List<UserCustomerCouponDO> listUserCustomerCouponAllByMap(Map map);

    List<UserCustomerCouponDO> listUserCustomerCouponByMap(Map map);

    Integer countUserCustomerCouponByMap(Map map);

    List<UserCustomerCouponDO> listUserCustomerGoodsCouponAllByMap(Map map);

    List<UserCustomerCouponDO> listUserCustomerGoodsCouponByMap(Map map);

    List<UserCustomerCouponDO> listUserCustomerSpecialCouponAllByMap(Map map);

    List<UserCustomerCouponDO> listUserCustomerSpecialCouponByMap(Map map);

    Integer listUserCustomerGoodsEnableCountCouponByMap(Map map);

    List<UserCustomerCouponDO> listUserCustomerGoodsEnableCouponByMap(Map map);

    List<CouponCustomerDO> selectMaxByMap(Map map);

    List<UserCustomerCouponDO> listByCouponNosAndCustomerId(@Param("customerId") Integer customerId,
        @Param("couponNos") List<String> couponNos);

    List<UserCustomerCouponDO> listByCouponNos(@Param("couponNos") List<String> couponNos);

    List<CouponCustomerDO> listByMap(Map map);

    List<UserCustomerCouponDO> listByCustomerId(@Param("customerId") Integer customerId);

    List<UserCustomerCouponDO> listCouponCustomerByCustomerIdsAndCouponCode(
        @Param("customerIds") List<Integer> customerIds, @Param("couponCode") String couponCode, @Param("couponStatus") Short couponStatus);
}