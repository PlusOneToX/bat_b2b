package com.bat.dubboapi.order.delivery.api;

import com.bat.dubboapi.order.delivery.dto.OrderDeliverBillCmd;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.delivery.dto.OrderDeliverBillRpcDTO;

import java.util.List;
public interface OrderDeliverBillServiceRpc {

    Response<List<OrderDeliverBillRpcDTO>> findNOLogisticsNoDeliverGoods(Long earlistTime);

    Response batchUpdate(List<OrderDeliverBillCmd> list);

    Response<OrderDeliverBillRpcDTO> findById(Integer id);

}
