package com.bat.order.service.cost;

import com.bat.order.service.cost.executor.OrderCustomerCostQryExe;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.cost.api.OrderCustomerCostServiceRpc;
import com.bat.dubboapi.order.cost.dto.OrderCustomerCostRpcQryDTO;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;

@DubboService
public class OrderCustomerCostServiceRpcImpl implements OrderCustomerCostServiceRpc {

    @Autowired
    private OrderCustomerCostQryExe orderCustomerCostQryExe;

    @Override
    public Response<OrderCustomerCostRpcQryDTO> getOrderCustomerCost(Integer orderId, Integer customerId) {
        OrderCustomerCostDO orderCustomerCostDO = orderCustomerCostQryExe.getOrderCustomerCost(orderId, customerId);
        if (orderCustomerCostDO != null) {
            OrderCustomerCostRpcQryDTO orderCustomerCostRpcQryDTO = new OrderCustomerCostRpcQryDTO();
            BeanUtils.copyProperties(orderCustomerCostDO, orderCustomerCostRpcQryDTO);
            return Response.of(orderCustomerCostRpcQryDTO);
        }
        return Response.of(null);
    }
}
