package com.bat.order.service.deliver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bat.order.service.deliver.convertor.OrderDeliveryConvertor;
import com.bat.order.service.deliver.executor.OrderDeliverBillQryExe;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.delivery.api.OrderDeliverBillServiceRpc;
import com.bat.dubboapi.order.delivery.dto.OrderDeliverBillCmd;
import com.bat.dubboapi.order.delivery.dto.OrderDeliverBillRpcDTO;
import com.bat.order.api.order.OrderGoodsServiceI;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.dao.deliver.dataobject.OrderDeliverBillDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;

@DubboService
public class OrderDeliverBillServiceRpcImpl implements OrderDeliverBillServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDeliverBillServiceRpcImpl.class);

    @Autowired
    private OrderDeliverBillQryExe orderDeliverBillQryExe;

    @Autowired
    private OrderInfoServiceI orderInfoServiceI;

    @Autowired
    private OrderGoodsServiceI orderGoodsServiceI;

    @Override
    public Response<List<OrderDeliverBillRpcDTO>> findNOLogisticsNoDeliverGoods(Long earlistTime) {
        List<OrderDeliverBillRpcDTO> orderDeliverBillRpcDTOS =
            orderDeliverBillQryExe.findNOLogisticsNoDeliverGoods(new Date(earlistTime));
        return Response.of(orderDeliverBillRpcDTOS);
    }

    @Override
    @Transactional
    public Response batchUpdate(List<OrderDeliverBillCmd> list) {
        List<OrderDeliverBillDO> dos = new ArrayList<>();
        if (list != null) {
            for (OrderDeliverBillCmd orderDeliverBillCmd : list) {
                OrderDeliverBillDO orderDeliverBillDO = new OrderDeliverBillDO();
                BeanUtils.copyProperties(orderDeliverBillCmd, orderDeliverBillDO);
                dos.add(orderDeliverBillDO);
            }

        }
        orderDeliverBillQryExe.batchUpdate(dos);

        if (dos.size() == 0) {
            return Response.buildSuccess();
        }
        // 处理order_info的发货状态
        List<Integer> orderIdList = dos.stream().map(OrderDeliverBillDO::getOrderId).collect(Collectors.toList());
        List<OrderInfoDO> orderInfoDOList = orderInfoServiceI.listByIds(orderIdList);
        if (orderInfoDOList == null || orderInfoDOList.size() == 0) {
            return Response.buildSuccess();
        }
        List<OrderGoodsDO> allOrderGoodsDOList = orderGoodsServiceI.listByOrderIdList(orderIdList);
        Map<Integer,
            List<OrderGoodsDO>> ordrGoodsMap = allOrderGoodsDOList.stream()
                .collect(Collectors.toMap(OrderGoodsDO::getOrderId, orderGoodsDO -> Lists.newArrayList(orderGoodsDO),
                    (List<OrderGoodsDO> oldList, List<OrderGoodsDO> newList) -> {
                        oldList.addAll(newList);
                        return oldList;
                    }));
        List<OrderDeliverBillDO> allOrderDeliverBillDOList = orderDeliverBillQryExe.listByOrderIdList(orderIdList);
        Map<Integer,
            List<OrderDeliverBillDO>> orderDeliverBillMap = allOrderDeliverBillDOList.stream()
                .collect(Collectors.toMap(OrderDeliverBillDO::getOrderId,
                    orderDeliverBillDO -> Lists.newArrayList(orderDeliverBillDO),
                    (List<OrderDeliverBillDO> oldList, List<OrderDeliverBillDO> newList) -> {
                        oldList.addAll(newList);
                        return oldList;
                    }));
        Map<Integer, OrderInfoDO> orderInfoDOMap =
            orderInfoDOList.stream().collect(Collectors.toMap(OrderInfoDO::getId, orderInfoDO -> orderInfoDO));
        dos.stream().forEach(orderDeliverBillDO -> {
            if (orderDeliverBillDO.getOrderId() != null && orderDeliverBillDO.getOrderId() > 0) {
                OrderInfoDO orderInfoDO = orderInfoDOMap.get(orderDeliverBillDO.getOrderId());
                List<OrderDeliverBillDO> orderDeliverBillDOList =
                    orderDeliverBillMap.get(orderDeliverBillDO.getOrderId());
                List<OrderGoodsDO> orderGoodsDOList = ordrGoodsMap.get(orderDeliverBillDO.getOrderId());
                OrderDeliveryConvertor.dealwithDeliverStatus(orderInfoDO, orderDeliverBillDO.getDeliverStatus(),
                    orderDeliverBillDOList, orderGoodsDOList, orderDeliverBillDO.getLogisticsNo(),
                    orderDeliverBillDO.getDeliverErpNo());
            }
        });
        orderInfoServiceI.batchUpdate(orderInfoDOList);
        return Response.buildSuccess();
    }

    @Override
    public Response<OrderDeliverBillRpcDTO> findById(Integer id) {
        OrderDeliverBillDO orderDeliverBillDO = orderDeliverBillQryExe.getById(id);
        OrderDeliverBillRpcDTO orderDeliverBillRpcDTO = new OrderDeliverBillRpcDTO();
        BeanUtils.copyProperties(orderDeliverBillDO, orderDeliverBillRpcDTO);
        return Response.of(orderDeliverBillRpcDTO);
    }
}
