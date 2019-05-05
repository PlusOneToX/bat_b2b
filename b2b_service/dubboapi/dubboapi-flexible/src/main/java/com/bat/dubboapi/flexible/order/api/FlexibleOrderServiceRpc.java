package com.bat.dubboapi.flexible.order.api;


import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnCodeQry;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnIdQry;
import com.bat.dubboapi.flexible.order.dto.OrderGoodsDiyParamDTO;

import java.util.List;

public interface FlexibleOrderServiceRpc {

    /**
     * 校验下单参数、基于ID
     * @param orderDetailBaseOnIdQryList
     * @return
     */
    Response<List<OrderGoodsDiyParamDTO>> validThirdOrderParamBaseId(List<OrderDetailBaseOnIdQry> orderDetailBaseOnIdQryList);

    /**
     * 校验下单参数、基于编码
     * @param orderDetailBaseOnCodeQries
     * @return
     */
    Response<List<OrderGoodsDiyParamDTO>> validThirdOrderParamBaseOnCode(List<OrderDetailBaseOnCodeQry> orderDetailBaseOnCodeQries);
}
