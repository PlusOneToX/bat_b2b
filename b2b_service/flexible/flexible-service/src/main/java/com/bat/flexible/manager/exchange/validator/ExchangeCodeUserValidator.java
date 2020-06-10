package com.bat.flexible.manager.exchange.validator;

import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.exchange.ExchangeCodeServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeCodeUserPageQry;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeUserDO;
import com.bat.flexible.manager.common.constant.exchange.ExchangeCodeConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;
import com.bat.flexible.manager.exchange.executor.code.ExchangeCodeUserQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeCodeUserValidator {


    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;

    @Autowired
    private ExchangeCodeUserQryExe exchangeCodeUserQryExe;

    /**
     * 校验绑定的暗码
     * @param secretCode
     * @return
     */
    public ExchangeCodeDO valid(String secretCode){
        ExchangeCodeDO exchangeCodeDO = exchangeCodeServiceI.findByPlainCodeAndSecretCode(null,secretCode);
        //判断是否可用
        if (exchangeCodeDO == null ) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_SECRET_CODE_ERROR);
        }
        if(!ExchangeCodeConstant.StatusUnUse.equals(exchangeCodeDO.getStatus())){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_STATUS_NOT_BELONG_UN_USE);
        }
        //判断是否被绑定
        ExchangeCodeUserDO exchangeCodeUser = exchangeCodeUserQryExe.getByExchangeCodeId(exchangeCodeDO.getId());
        if (exchangeCodeUser != null) {
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_USER_BIND_REPEAT);
        }
        return exchangeCodeDO;
    }

    public void validCountParam(ExchangeCodeUserPageQry exchangeCodeUserPageQry) {
        if(exchangeCodeUserPageQry.getUserId() ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_USER_ID_NULL);
        }
        if(exchangeCodeUserPageQry.getStatus() ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_OPEN_FLAG_NULL);
        }
    }
}
