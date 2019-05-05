package com.bat.dubboapi.order.type.api;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.type.dto.OrderTypeRpcQryDTO;

public interface OrderTypeServiceRpc {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    Response<OrderTypeRpcQryDTO> getById(Integer id);

    /**
     * 根据特殊类型获取订单类型
     *
     * @param specialFlag
     * @return
     */
    Response<OrderTypeRpcQryDTO> getBySpecialFlag(Short specialFlag);

}
