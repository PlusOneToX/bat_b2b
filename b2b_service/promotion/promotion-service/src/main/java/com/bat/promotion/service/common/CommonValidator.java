package com.bat.promotion.service.common;

import static com.bat.promotion.service.user.executor.ErrorCode.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bat.promotion.api.base.PromotionException;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/24 17:32
 */
@Component
public class CommonValidator {

    public void checkCustomer(String customerId, String distributorId) {
        if (StringUtils.isBlank(customerId)) {
            throw PromotionException.buildException(B_USER_LOGIN_ERROR);
        }
        if (StringUtils.isBlank(distributorId)) {
            throw PromotionException.buildException(B_USER_DISTRIBUTOR_ID_ERROR);
        }
    }

    public void checkCustomer(String customerId, String distributorId, String platform) {
        checkCustomer(customerId, distributorId);
        if (StringUtils.isBlank(platform)) {
            throw PromotionException.buildException(B_PROMOTION_PLATFORM_NULL);
        }
    }

}
