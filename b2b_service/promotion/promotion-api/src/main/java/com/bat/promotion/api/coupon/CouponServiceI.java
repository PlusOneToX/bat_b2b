package com.bat.promotion.api.coupon;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.coupon.dto.*;
import com.bat.promotion.api.coupon.dto.data.CouponCustomerDTO;
import com.bat.promotion.api.coupon.dto.data.CouponDTO;
import com.bat.promotion.api.coupon.dto.data.CouponListDTO;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.coupon.dto.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:25
 */
public interface CouponServiceI {
    /**
     * 根据搜索条件查找优惠券列表(分页)
     * 
     * @param qry
     * @return
     */
    PageInfo<CouponListDTO> listCoupon(CouponListQry qry);

    /**
     * 新增优惠券活动
     * 
     * @param cmd
     */
    void createCoupon(CouponCmd cmd, String userId, String userName);

    /**
     * 修改优惠券活动(草稿状态的优惠券修改)
     * 
     * @param cmd
     */
    void updateCoupon(CouponCmd cmd, String userId, String userName);

    /**
     * 修改优惠券发放总数量和限购数量
     * 
     * @param cmd
     */
    void updateCouponCount(CouponCountCmd cmd);

    /**
     * 根据id查询优惠券详情
     * 
     * @param qry
     * @return
     */
    CouponDTO getCoupon(BaseId qry);

    /**
     * 根据id删除优惠券
     * 
     * @param cmd
     */
    void deleteCoupon(BaseId cmd);

    /**
     * 根据优惠券id变更状态
     * 
     * @param cmd
     */
    void updateCouponStatus(CouponStatusCmd cmd);

    /**
     * 获取券码列表
     * 
     * @param qry
     */
    PageInfo<CouponCustomerDTO> listCouponNo(CouponCustomerListQry qry);

    /**
     * 根据券码变更客户优惠券状态
     * 
     * @param cmd
     */
    void updateCustomerCouponStatus(CouponCustomerStatusCmd cmd);

}
