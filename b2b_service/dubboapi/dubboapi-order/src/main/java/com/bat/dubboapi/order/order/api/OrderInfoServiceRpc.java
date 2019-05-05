package com.bat.dubboapi.order.order.api;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.order.dto.erp.ErpOrderDetailEntryId;
import com.bat.dubboapi.order.order.dto.info.OrderInfoRpcQryDTO;

import java.util.List;

public interface OrderInfoServiceRpc {


    /**
     * 根据主键id查询订单信息
     */
    Response<OrderInfoRpcQryDTO> getById(Integer orderId);

    /**
     * 订单同步ERP成功之后、处理B2B订单数据 、返回true表示第一次同步、返回false表示多次同步
     * @param entryIds
     * @param orderErpNo
     * @param orderId
     * @param erpOrderType ERP实际的订单类型（订单类型可能于B2B的orderInfo的order_type_id不一致）
     * @param syncVoucherFlag 是否需要发消息同步收款单到ERP
     * @return
     */
    Response<Boolean> setOrderErpMsg(List<ErpOrderDetailEntryId> entryIds, String orderErpNo, Integer orderId, String erpOrderType, Boolean syncVoucherFlag);

    /**
     * 根据B2B订单号查询订单信息
     * @param orderNo
     * @return
     */
    Response<OrderInfoRpcQryDTO> getByOrderNo(String orderNo);
}
