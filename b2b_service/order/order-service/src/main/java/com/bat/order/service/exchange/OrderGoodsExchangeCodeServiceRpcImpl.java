package com.bat.order.service.exchange;

import java.util.List;

import com.bat.order.service.exchange.executor.OrderGoodsExchangeCodeQryExe;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.exchange.api.OrderGoodsExchangeCodeServiceRpc;
import com.bat.dubboapi.order.exchange.dto.OrderGoodsExchangeCodeRpcQryDTO;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.dao.order.dataobject.OrderGoodsExchangeCodeDO;

@DubboService
public class OrderGoodsExchangeCodeServiceRpcImpl implements OrderGoodsExchangeCodeServiceRpc {

    @Autowired
    private OrderGoodsExchangeCodeQryExe orderGoodsExchangeCodeQryExe;

    @Override
    public Response<List<OrderGoodsExchangeCodeRpcQryDTO>> listByOrderId(Integer orderId) {
        List<OrderGoodsExchangeCodeDO> list = orderGoodsExchangeCodeQryExe.listByOrderId(orderId);
        return Response.of(BeanUtils.copyList(list, OrderGoodsExchangeCodeRpcQryDTO.class));
    }
}
