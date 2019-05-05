package com.bat.dubboapi.flexible.exchange;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.exchange.dto.*;
import com.bat.dubboapi.flexible.common.ResponseBaseBean;

import java.util.List;

public interface ExchangeCardServiceRpc {



    ResponseBaseBean bindOrderAndBoxCode(OrderExchangeCardBindErpRequest orderExchangeCardBindErpRequest);

    /**
     * 校验柔性下单的兑换码参数合法性
     * @param exchangeCodeOrderDTOList
     * @return
     */
    Response<List<ExchangeCodeSimpleDTO>> checkRxExchange(List<ExchangeCodeOrderDTO> exchangeCodeOrderDTOList, Short shareFlag);

    ResponseBaseBean refund(ExchangeCodeRefundDTO exchangeCodeRefundDTO);

    /**
     * 校验货品id是否为兑换卡活动
     * @param itemId 货品id
     * @param distributorId 分销商id
     * @return
     */
    Response<ExchangeCardDTORpc> checkItemIsExchange(Integer itemId, Integer distributorId);

    /**
     * 根据兑换卡活动id查询
     * @param exchangeId
     * @return
     */
    Response<ExchangeCardDTORpc> getById(Integer exchangeId);
}
