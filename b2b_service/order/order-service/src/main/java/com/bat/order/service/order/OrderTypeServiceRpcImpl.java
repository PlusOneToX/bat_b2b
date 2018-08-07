package com.bat.order.service.order;

import java.util.List;

import com.bat.order.service.common.CommonErrorCode;
import com.bat.order.service.order.convertor.OrderTypeConvertor;
import com.bat.order.service.order.executor.OrderTypeQryExe;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.type.api.OrderTypeServiceRpc;
import com.bat.dubboapi.order.type.dto.OrderTypeRpcQryDTO;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.order.dataobject.OrderTypeDO;

@DubboService
public class OrderTypeServiceRpcImpl implements OrderTypeServiceRpc {

    @Autowired
    private OrderTypeQryExe orderTypeQryExe;

    @Override
    public Response<OrderTypeRpcQryDTO> getById(Integer id) {
        OrderTypeDO orderTypeDO = orderTypeQryExe.getById(id);
        OrderTypeRpcQryDTO orderTypeRpcQryDTO = OrderTypeConvertor.toOrderTypeRpcQryDTO(orderTypeDO);
        return Response.of(orderTypeRpcQryDTO);
    }

    @Override
    public Response<OrderTypeRpcQryDTO> getBySpecialFlag(Short specialFlag) {
        List<OrderTypeDO> orderTypeDOS = orderTypeQryExe.listBySpecialFlag(specialFlag);
        if (!CollectionUtils.isEmpty(orderTypeDOS)) {
            OrderTypeRpcQryDTO orderType = OrderTypeConvertor.toOrderTypeRpcQryDTO(orderTypeDOS.get(0));
            return Response.of(orderType);
        }
        return Response.buildFailure(CommonErrorCode.B_ORDER_TYPE_CASH_NULL, MessageUtils.get(CommonErrorCode.B_ORDER_TYPE_CASH_NULL));
    }
}
