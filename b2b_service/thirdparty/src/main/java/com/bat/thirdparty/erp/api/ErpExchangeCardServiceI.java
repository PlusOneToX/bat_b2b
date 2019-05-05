package com.bat.thirdparty.erp.api;

import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeRefundDTO;
import com.bat.dubboapi.flexible.exchange.dto.OrderExchangeCardBindErpRequest;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;

public interface ErpExchangeCardServiceI {
    ResponseBaseBean bindOrderAndBoxCode(OrderExchangeCardBindErpRequest orderExchangeCardBindErpRequest);

    ResponseBaseBean refund(ExchangeCodeRefundDTO exchangeCodeRefundDTO);
}
