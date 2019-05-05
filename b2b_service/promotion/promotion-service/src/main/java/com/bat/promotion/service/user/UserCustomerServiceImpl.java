package com.bat.promotion.service.user;

import java.util.List;

import javax.annotation.Resource;

import com.bat.promotion.api.user.dto.customer.*;
import com.bat.promotion.service.common.enumtype.ReceiveStatus;
import com.bat.promotion.service.user.executor.UserCustomerCmdExe;
import com.bat.promotion.service.user.executor.UserCustomerQryExe;
import com.bat.promotion.api.user.dto.customer.data.UserCustomerCouponCountDTO;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseIds;
import com.bat.promotion.api.user.UserCustomerServiceI;
import com.bat.promotion.api.user.dto.customer.*;
import com.bat.promotion.api.user.dto.customer.data.UserCustomerCouponDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/27 8:42
 */
@Service
public class UserCustomerServiceImpl implements UserCustomerServiceI {

    @Resource
    private UserCustomerCmdExe cmdExe;
    @Resource
    private UserCustomerQryExe qryExe;

    @Override
    public PageInfo<UserCustomerCouponDTO> couponList(UserCustomerCouponListQry qry, String userId,
                                                      String distributorId) {
        return qryExe.couponList(qry, userId, distributorId);
    }

    @Override
    public UserCustomerCouponCountDTO couponCount(String userId, String distributorId) {
        UserCustomerCouponCountDTO dto = new UserCustomerCouponCountDTO();
        UserCustomerCouponListQry qry = new UserCustomerCouponListQry();

        //待领取
        qry.setStatuss(ReceiveStatus.UN_RECEIVE.getValue().toString());
        int count = qryExe.couponCount(qry, userId, distributorId);
        dto.setUnReceiveCount(count);

        //未使用
        qry.setStatuss(ReceiveStatus.UN_USED.getValue().toString());
        count = qryExe.couponCount(qry, userId, distributorId);
        dto.setUnUsedCount(count);

        //已使用
        qry.setStatuss(ReceiveStatus.USED.getValue().toString());
        count = qryExe.couponCount(qry, userId, distributorId);
        dto.setUsedCount(count);

        //已过期
        qry.setStatuss(ReceiveStatus.EXPIRED.getValue().toString());
        count = qryExe.couponCount(qry, userId, distributorId);
        dto.setExpiredCount(count);
        return dto;
    }


    @Override
    public PageInfo<UserCustomerCouponDTO> couponGoodsList(UserCustomerGoodsCouponListQry qry, String userId,
                                                           String distributorId) {
        return qryExe.couponGoodsList(qry, userId, distributorId);
    }

    @Override
    public List<UserCustomerCouponDTO> couponSpecialList(UserCustomerSpecialCouponListQry qry, String userId,
                                                         String distributorId) {
        return qryExe.couponSpecialList(qry, userId, distributorId);
    }

    @Override
    public Integer couponGoodsEnableCount(UserCustomerGoodsEnableCouponQry qry, String userId, String distributorId) {
        return qryExe.couponGoodsEnableCount(qry, userId, distributorId);
    }

    @Override
    public UserCustomerCouponDTO couponGoodsOptimal(UserCustomerGoodsOptimalCouponQry qry, String userId,
                                                    String distributorId) {
        return qryExe.couponGoodsOptimal(qry, userId, distributorId);
    }

    @Override
    public void receiveCoupon(BaseIds cmd, String userId, String userName, String distributorId, String platform,
        String openId) {
        cmdExe.receiveCoupon(cmd, userId, userName, distributorId, platform, openId);
    }
}
