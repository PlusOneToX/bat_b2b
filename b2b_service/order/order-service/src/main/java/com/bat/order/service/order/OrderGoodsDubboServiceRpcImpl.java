package com.bat.order.service.order;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.api.OrderGoodsDubboServiceRpc;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsDetailRpcDTO;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsRpcDTO;
import com.bat.order.api.common.utils.BeanUtils;
import com.bat.order.api.order.OrderGoodsServiceI;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDetailDTO;

@DubboService
public class OrderGoodsDubboServiceRpcImpl implements OrderGoodsDubboServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderGoodsDubboServiceRpcImpl.class);

    @Autowired
    private OrderGoodsServiceI orderGoodsServiceI;

    @Override
    public Response<List<OrderGoodsRpcDTO>> listOrderGoodsByOrderId(Integer orderId) {
        List<OrderGoodsDO> orderGoodsDOList = orderGoodsServiceI.listByOrderId(orderId);
        List<OrderGoodsRpcDTO> list = BeanUtils.copyList(orderGoodsDOList, OrderGoodsRpcDTO.class);
        return Response.of(list);
    }

    @Override
    public Response<List<OrderGoodsDetailRpcDTO>> listOrderGoodsDetailByOrderId(Integer orderId) {
        List<OrderGoodsDetailDTO> orderGoodsDetailDTOS = orderGoodsServiceI.listOrderGoodsDetailByOrderId(orderId);
        List<OrderGoodsDetailRpcDTO> list = BeanUtils.copyList(orderGoodsDetailDTOS, OrderGoodsDetailRpcDTO.class);
        if (list == null) {
            list = new ArrayList<>();
        }
        return Response.of(list);
    }
}
