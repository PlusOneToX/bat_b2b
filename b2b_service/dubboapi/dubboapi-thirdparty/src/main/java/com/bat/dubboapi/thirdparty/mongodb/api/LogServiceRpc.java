package com.bat.dubboapi.thirdparty.mongodb.api;

import com.bat.dubboapi.thirdparty.mongodb.dto.OrderLogCmd;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.OrderDeliverBillLogDTO;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.OrderLogDTO;
import com.bat.dubboapi.thirdparty.common.Response;

import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/9 8:48
 */
public interface LogServiceRpc {

    /**
     * 创建订单操作日志
     *
     * @param cmd
     */
    Response createOrderLog(OrderLogCmd cmd);

    /**
     * 根据订单id获取操作日志
     *
     * @param orderId
     * @return
     */
    Response<List<OrderLogDTO>> findOrderLogByOrderId(Integer orderId);

    /**
     * 根据发货单ids查找发货单日志
     * @param ids
     * @return
     */
    Response<List<OrderDeliverBillLogDTO>> findOrderDeliverBillLogByIdsAndOperateType(List<Integer> ids, String operateType);
}
