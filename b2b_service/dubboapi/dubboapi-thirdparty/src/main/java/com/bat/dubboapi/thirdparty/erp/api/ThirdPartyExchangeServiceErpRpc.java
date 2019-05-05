package com.bat.dubboapi.thirdparty.erp.api;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.dto.exchange.ExchangeCodeInvalidCodeDTO;
import com.bat.dubboapi.thirdparty.erp.dto.exchange.ExchangeCodeSyncERPRequest;

public interface ThirdPartyExchangeServiceErpRpc {

    /**
     * 同步作废的兑换码到ERP
     * @param exchangeCodeInvalidCodeDTO
     * @return
     */
    Response syncExchangeCodeInvalidToErp(ExchangeCodeInvalidCodeDTO exchangeCodeInvalidCodeDTO);

    /**
     * 同步盒码明码到ERP
     * @param exchangeCodeSyncERPRequest
     * @return
     */
    Response syncBoxCodeAndPlainCodeToErp(ExchangeCodeSyncERPRequest exchangeCodeSyncERPRequest);
}
