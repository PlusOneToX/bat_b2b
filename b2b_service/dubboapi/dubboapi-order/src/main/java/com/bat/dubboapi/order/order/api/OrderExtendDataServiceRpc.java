package com.bat.dubboapi.order.order.api;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.dto.data.OrderExtendDataSimpleRpcDTO;

import java.util.List;

public interface OrderExtendDataServiceRpc {

    /**
     * 根据订单id列表查询订单拓展信息列表
     * @param orderIdList 订单id列表
     * @return
     */
    Response<List<OrderExtendDataSimpleRpcDTO>> listExtendDataSimpleByOrderIdList(List<Integer> orderIdList);

    /**
     * 根据订单id查询订单拓展信息
     * @param orderId 订单id
     * @return
     */
    Response<OrderExtendDataSimpleRpcDTO> getExtendDataSimpleByOrderId(Integer orderId);

    Response<OrderExtendDataSimpleRpcDTO> getByOrderFactoryNo(String maikeOrderNo);

    /**
     * 设置工厂单号
     * @param orderId
     * @param otherFactoryNo
     * @param manufactors 工厂编码
     * @return
     */
    Response setOtherFactoryNo(Integer orderId, String otherFactoryNo,String manufactors);
}
