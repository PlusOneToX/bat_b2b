package com.bat.order.service.data;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderCustomerDataServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderCustomerDataRpcDTO;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.dao.data.dataobject.OrderCustomerDataDO;
import com.bat.order.service.data.executor.OrderCustomerDataQryExe;

@DubboService
public class OrderCustomerDataServiceRpcImpl implements OrderCustomerDataServiceRpc {

    @Autowired
    private OrderCustomerDataQryExe orderCustomerDataQryExe;

    @Override
    public Response<OrderCustomerDataRpcDTO> getByOrderId(Integer orderId) {
        OrderCustomerDataDO orderCustomerData = orderCustomerDataQryExe.getOrderCustomerData(orderId);
        return Response.of(BeanUtils.copy(orderCustomerData, OrderCustomerDataRpcDTO.class));
    }
}
