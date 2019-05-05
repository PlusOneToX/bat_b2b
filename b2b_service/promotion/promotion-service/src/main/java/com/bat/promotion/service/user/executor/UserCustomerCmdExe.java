package com.bat.promotion.service.user.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.service.common.CommonUtils;
import com.bat.promotion.service.common.CommonValidator;
import com.bat.promotion.service.message.MessageSendService;
import com.bat.promotion.service.user.convertor.UserConvertor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.promotion.api.base.BaseIds;
import com.bat.promotion.dao.coupon.CouponCustomerMapper;
import com.bat.promotion.dao.coupon.CouponMapper;
import com.bat.promotion.dao.coupon.dataobject.CouponCustomerDO;
import com.bat.promotion.dao.coupon.dataobject.CouponDO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/27 8:44
 */
@Component
public class UserCustomerCmdExe {

    @Resource
    private CommonValidator commonValidator;

    @Resource
    private CouponCustomerMapper couponCustomerMapper;

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private MessageSendService messageSendService;

    @Value("${sanxing.distributorId}")
    private Integer sanxingDistributorId;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCustomerCmdExe.class);

    /**
     * 用户主动领取优惠券
     * 
     * @param cmd
     * @param userId
     * @param distributorId
     * @param platform
     */
    @Transactional(rollbackFor = Exception.class)
    public void receiveCoupon(BaseIds cmd, String userId, String userName, String distributorId, String platform,
        String openId) {
        commonValidator.checkCustomer(userId, distributorId, platform);
        // 同样的券 一次只能领一张
        List<Integer> couponIds = cmd.getIds().stream().distinct().collect(Collectors.toList());
        List<CouponDO> couponDOS = couponMapper.listCouponByIds(couponIds);
        Map<String, Object> qryMap = new HashMap();
        qryMap.put("customerId", Integer.valueOf(userId));
        qryMap.put("couponIds", couponIds);
        List<CouponCustomerDO> couponCustomerDOS = couponCustomerMapper.selectMaxByMap(qryMap);
        List<CouponCustomerDO> customerDOS = UserConvertor.toCouponCustomerDOList(Integer.valueOf(userId), userName,
            Integer.valueOf(distributorId), platform, openId, couponDOS, couponCustomerDOS);
        saveCouponCustomer(customerDOS);
        updateCoupon(couponDOS);
    }

    /**
     * 保持领取的优惠券（处理优惠券码唯一性）
     * 
     * @param customerDOS
     */
    private void saveCouponCustomer(List<CouponCustomerDO> customerDOS) {
        try {
            customerDOS.forEach(customerDO -> customerDO.setCouponNo(CommonUtils.getRandom()));
            if (!CollectionUtils.isEmpty(customerDOS)) {
                couponCustomerMapper.insertList(customerDOS);
                synCouponbat(customerDOS);
            }
        } catch (DuplicateKeyException e) {
            saveCouponCustomer(customerDOS);
        }
    }

    /**
     * 更新 领取数量
     * 
     * @param couponDOS
     */
    private void updateCoupon(List<CouponDO> couponDOS) {
        for (CouponDO couponDO : couponDOS) {
            couponMapper.updateByPrimaryKey(couponDO);
        }
    }

    /**
     * 发送同步优惠卷消息
     * 
     * @param couponCustomerDOS
     */
    public void synCouponbat(List<CouponCustomerDO> couponCustomerDOS) {
        try {
            if (couponCustomerDOS.size() > 0) {
                StringBuilder userIds = new StringBuilder();
                for (CouponCustomerDO couponCustomerDO : couponCustomerDOS) {
                    if (couponCustomerDO.getDistributorId().intValue() == sanxingDistributorId) {
                        userIds.append(couponCustomerDO.getCustomerId()).append(",");
                    }
                }
                if (StringUtils.isBlank(userIds)) {
                    return;
                }
                userIds = new StringBuilder(userIds.substring(0, userIds.length() - 1));
                messageSendService.synCouponbat(userIds.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
