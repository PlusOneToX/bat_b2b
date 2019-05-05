package com.bat.promotion.service.coupon;

import javax.annotation.Resource;

import com.bat.promotion.api.coupon.dto.*;
import com.bat.promotion.service.coupon.executor.CouponCmdExe;
import com.bat.promotion.service.coupon.executor.CouponQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.coupon.CouponServiceI;
import com.bat.promotion.api.coupon.dto.*;
import com.bat.promotion.api.coupon.dto.data.CouponCustomerDTO;
import com.bat.promotion.api.coupon.dto.data.CouponDTO;
import com.bat.promotion.api.coupon.dto.data.CouponListDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/24 10:41
 */
@Service
public class CouponServiceImpl implements CouponServiceI {
    @Resource
    private CouponCmdExe cmdExe;
    @Resource
    private CouponQryExe qryExe;

    @Override
    public PageInfo<CouponListDTO> listCoupon(CouponListQry qry) {
        return qryExe.listCoupon(qry);
    }

    @Override
    public void createCoupon(CouponCmd cmd, String userId, String userName) {
        cmdExe.createCoupon(cmd, userId, userName);
    }

    @Override
    public void updateCoupon(CouponCmd cmd, String userId, String userName) {
        cmdExe.updateCoupon(cmd, userId, userName);
    }

    @Override
    public void updateCouponCount(CouponCountCmd cmd) {
        cmdExe.updateCouponCount(cmd);
    }

    @Override
    public CouponDTO getCoupon(BaseId qry) {
        return qryExe.getCoupon(qry);
    }

    @Override
    public void deleteCoupon(BaseId cmd) {
        cmdExe.deleteCoupon(cmd);
    }

    @Override
    public void updateCouponStatus(CouponStatusCmd cmd) {
        cmdExe.updateCouponStatus(cmd);
    }

    @Override
    public PageInfo<CouponCustomerDTO> listCouponNo(CouponCustomerListQry qry) {
        return qryExe.listCouponNo(qry);
    }

    @Override
    public void updateCustomerCouponStatus(CouponCustomerStatusCmd cmd) {
        cmdExe.updateCustomerListCouponStatus(cmd);
    }
}
