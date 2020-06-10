package com.bat.flexible.manager.exchange.validator;

import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import com.bat.flexible.manager.common.constant.exchange.ExchangeCodeConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeRxErrorConstant;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangeCodeValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeCodeValidator.class);

    public static void validExchangeCodeUse(ExchangeCodeDO exchangeCodeDO,String secretCode){
        if(exchangeCodeDO ==null){
            LOGGER.info("暗码错误,{}",secretCode);
            throw FlexibleDubboApiException.buildException(secretCode+ MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_SECRET_CODE_ERROR));
        }
        if(ExchangeCodeConstant.StatusEnd.equals(exchangeCodeDO.getStatus())){
            //已过期
            LOGGER.info("暗码已经过期、无法完成兑换,{}",secretCode);
            throw FlexibleDubboApiException.buildException(secretCode+MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_STATUS_END));
        }
        if(!ExchangeCodeConstant.StatusUnUse.equals(exchangeCodeDO.getStatus())){
            //已过期
            LOGGER.info("暗码不属于未使用、无法完成兑换,{}",secretCode);
            throw FlexibleDubboApiException.buildException(secretCode+MessageUtils.get(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_STATUS_NOT_BELONG_UN_USE));
        }
    }

    /**
     * 判断用户下单兑换码状态是否可用
     * @param exchangeCodeDO
     * @param secortCode
     */
    public static void validExchangeCodeStatusWhenUserUse(ExchangeCodeDO exchangeCodeDO,String secortCode){
        if(exchangeCodeDO ==null){
            LOGGER.error("暗码错误{}",secortCode);
            throw FlexibleDubboApiException.buildException(ExchangeRxErrorConstant.SecretCodeError.getCode()+"",ExchangeRxErrorConstant.SecretCodeError.getMsg()+"【"+secortCode+"】");
        }
        if(exchangeCodeDO.getStatus()-ExchangeCodeConstant.StatusInit ==0){
            LOGGER.error("暗码错误{}未激活",secortCode);
            throw FlexibleDubboApiException.buildException(ExchangeRxErrorConstant.ExchangeFailByCodeStatusInit.getCode()+"","【"+secortCode+"】"+ExchangeRxErrorConstant.ExchangeFailByCodeStatusInit.getMsg());
        }
        if(exchangeCodeDO.getStatus()-ExchangeCodeConstant.StatusUsed ==0){
            LOGGER.error("暗码错误{}已使用",secortCode);
            throw FlexibleDubboApiException.buildException(ExchangeRxErrorConstant.ExchangeFailByCodeStatusUsed.getCode()+"","【"+secortCode+"】"+ExchangeRxErrorConstant.ExchangeFailByCodeStatusUsed.getMsg());
        }
        if(exchangeCodeDO.getStatus()-ExchangeCodeConstant.StatusEnd ==0){
            LOGGER.error("暗码错误{}已过期",secortCode);
            throw FlexibleDubboApiException.buildException(ExchangeRxErrorConstant.ExchangeFailByCodeStatusOverdue.getCode()+"","【"+secortCode+"】"+ExchangeRxErrorConstant.ExchangeFailByCodeStatusOverdue.getMsg());
        }
        if(exchangeCodeDO.getStatus()-ExchangeCodeConstant.StatusInvalid ==0){
            LOGGER.error("暗码错误{}已作废",secortCode);
            throw FlexibleDubboApiException.buildException(ExchangeRxErrorConstant.ExchangeFailByCodeStatusInvalid.getCode()+"","【"+secortCode+"】"+ExchangeRxErrorConstant.ExchangeFailByCodeStatusInvalid.getMsg());
        }
    }
}
