package com.bat.flexible.manager.exchange.validator;

import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 沙漠
 */
@Component
public class CommonValidator {


    /**
     * 检查用户id不能为空且归属分销商不能为空
     * 
     * @param userId
     * @param distributorId
     */
    public void checkUserId(String userId, String distributorId) {
        if (StringUtils.isBlank(userId)) {
            throw new FlexibleCustomException(
                    MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_USER)
                            + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR));
        }
        if (StringUtils.isBlank(distributorId)) {
            throw new FlexibleCustomException(
                    MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_DISTRIBUTOR)
                            + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR));
        }
    }




}
