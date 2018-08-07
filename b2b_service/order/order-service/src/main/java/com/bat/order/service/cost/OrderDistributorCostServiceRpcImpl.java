package com.bat.order.service.cost;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.cost.api.OrderDistributorCostServiceRpc;
import com.bat.dubboapi.order.cost.dto.OrderDistributorCostRpcQryDTO;
import com.bat.dubboapi.order.cost.dto.OrderGoodsDistributorCostRpcQryDTO;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.cost.OrderDistributorCostServiceI;
import com.bat.order.api.cost.OrderGoodsDistributorCostServiceI;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;

@DubboService
public class OrderDistributorCostServiceRpcImpl implements OrderDistributorCostServiceRpc {

    @Autowired
    private OrderDistributorCostServiceI orderDistributorCostServiceI;

    @Autowired
    private OrderGoodsDistributorCostServiceI orderGoodsDistributorCostServiceI;

    @Override
    public Response<OrderDistributorCostRpcQryDTO> getByOrderIdAndDistributorId(Integer orderId,
        Integer distributorId) {
        OrderDistributorCostDO orderDistributorCostDO =
            orderDistributorCostServiceI.getByOrderIdAndDistributorId(orderId, distributorId);
        return Response.of(BeanUtils.copy(orderDistributorCostDO, OrderDistributorCostRpcQryDTO.class));
    }

    /**
     * 根据订单id和分销商id查询订单明细归属分销商的费用
     * 
     * @param orderId
     * @param distributorId
     * @return
     */
    @Override
    public Response<List<OrderGoodsDistributorCostRpcQryDTO>> listGoodsCostByOrderIdAndDistributorId(Integer orderId,
        Integer distributorId) {
        List<OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOList =
            orderGoodsDistributorCostServiceI.listByOrderIdAndDistributorId(orderId, distributorId);
        return Response
            .of(BeanUtils.copyList(orderGoodsDistributorCostDOList, OrderGoodsDistributorCostRpcQryDTO.class));
    }

    @Override
    public Response<Boolean> checkIsGroup(Integer orderId) {
        Boolean flag = orderGoodsDistributorCostServiceI.checkIsGroup(orderId);
        return Response.of(flag);
    }

    @Override
    public Response<List<OrderDistributorCostRpcQryDTO>> getDirectPayInfoByOrderId(Integer orderId) {
        List<OrderDistributorCostDO> list = orderDistributorCostServiceI.getDirectPayInfoByOrderId(orderId);
        return Response.of(BeanUtils.copyList(list, OrderDistributorCostRpcQryDTO.class));
    }
}
