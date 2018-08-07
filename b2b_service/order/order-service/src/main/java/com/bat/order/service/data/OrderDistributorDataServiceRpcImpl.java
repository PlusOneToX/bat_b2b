package com.bat.order.service.data;

import java.util.List;

import com.bat.order.service.common.error.OrderDistributorErrorCode;
import com.bat.order.service.data.executor.OrderDistributorDataQryExe;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderDistributorDataServiceRpc;
import com.bat.dubboapi.order.order.dto.data.OrderDistributorDataRpcDTO;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;

@DubboService
public class OrderDistributorDataServiceRpcImpl implements OrderDistributorDataServiceRpc {

    @Autowired
    private OrderDistributorDataQryExe orderDistributorDataQryExe;

    @Override
    public Response<List<OrderDistributorDataRpcDTO>> getByOrderErpNoAndErpFlag(String orderErpNo, Short erpFlag) {
        List<OrderDistributorDataDO> orderDistributorDataDOList =
            orderDistributorDataQryExe.getByOrderErpNoAndErpFlag(orderErpNo, erpFlag);
        return Response.of(BeanUtils.copyList(orderDistributorDataDOList, OrderDistributorDataRpcDTO.class));
    }

    @Override
    public Response<List<OrderDistributorDataRpcDTO>> listByOrderIdAndErpFlag(Integer orderId, Short erpFlag) {
        List<OrderDistributorDataDO> orderDistributorDataDOList =
            orderDistributorDataQryExe.listByCondition(orderId, null, erpFlag);
        return Response.of(BeanUtils.copyList(orderDistributorDataDOList, OrderDistributorDataRpcDTO.class));
    }

    /**
     * 根据订单id和分销商id查询订单归属分销商数据
     * 
     * @param orderId
     * @param distributorId
     * @return
     */
    @Override
    public Response<OrderDistributorDataRpcDTO> getByOrderIdAndDistributorId(Integer orderId, Integer distributorId) {
        OrderDistributorDataDO orderDistributorData =
            orderDistributorDataQryExe.getOrderDistributorData(orderId, distributorId);
        if (orderDistributorData == null) {
            return Response.buildFailure(OrderDistributorErrorCode.O_ORDER_DISTRIBUTOR_DATA_NULL,
                MessageUtils.get(OrderDistributorErrorCode.O_ORDER_DISTRIBUTOR_DATA_NULL));
        }
        return Response.of(BeanUtils.copy(orderDistributorData, OrderDistributorDataRpcDTO.class));
    }
}
