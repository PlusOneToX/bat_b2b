package com.bat.promotion.api.user;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseIds;
import com.bat.promotion.api.user.dto.customer.*;
import com.bat.promotion.api.user.dto.customer.data.UserCustomerCouponCountDTO;
import com.bat.promotion.api.user.dto.customer.data.UserCustomerCouponDTO;
import com.bat.promotion.api.user.dto.customer.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:25
 */
public interface UserCustomerServiceI {
    /**
     * 优惠券列表(分页)
     * 
     * @param qry
     * @param userId
     * @param distributorId
     * @return
     */
    PageInfo<UserCustomerCouponDTO> couponList(UserCustomerCouponListQry qry, String userId, String distributorId);

    /**
     * 优惠券总数
     *
     * @param userId
     * @param distributorId
     * @return
     */
    UserCustomerCouponCountDTO couponCount(String userId, String distributorId);

    /**
     * 根据商品列表获取优惠券列表(分页)
     * 
     * @param qry
     * @param userId
     * @param distributorId
     * @return
     */
    PageInfo<UserCustomerCouponDTO> couponGoodsList(UserCustomerGoodsCouponListQry qry, String userId,
                                                    String distributorId);

    /**
     * 根据优惠券类型获取优惠券列表(不分页)
     * 
     * @param qry
     * @param userId
     * @param distributorId
     * @return
     */
    List<UserCustomerCouponDTO> couponSpecialList(UserCustomerSpecialCouponListQry qry, String userId,
                                                  String distributorId);

    /**
     * 根据商品列表获取可用优惠券列表数量
     * 
     * @param qry
     * @param userId
     * @param distributorId
     * @return
     */
    Integer couponGoodsEnableCount(UserCustomerGoodsEnableCouponQry qry, String userId, String distributorId);

    /**
     * 根据商品列表获取最优优惠券
     * 
     * @param qry
     * @param userId
     * @param distributorId
     * @return
     */
    UserCustomerCouponDTO couponGoodsOptimal(UserCustomerGoodsOptimalCouponQry qry, String userId,
                                             String distributorId);

    /**
     * 用户主动领取优惠券
     * 
     * @param cmd
     * @param userId
     * @param distributorId
     * @param platform
     */
    void receiveCoupon(BaseIds cmd, String userId, String userName, String distributorId, String platform,
                       String openId);

}
