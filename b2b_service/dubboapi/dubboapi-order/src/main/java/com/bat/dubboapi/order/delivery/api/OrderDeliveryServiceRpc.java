package com.bat.dubboapi.order.delivery.api;

import java.util.Date;
import java.util.List;

import com.bat.dubboapi.order.delivery.dto.*;
import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.common.ResponseBaseBean;
import com.bat.dubboapi.order.order.dto.OrderCancelSyncParam;

public interface OrderDeliveryServiceRpc {

    /**
     * 发货
     *
     * @param orderDeliveryCmd
     * @return
     */
    ResponseBaseBean deliverOrder(OrderDeliveryCmd orderDeliveryCmd);

    /**
     * 根据订单id查询订单送货信息表
     */
    Response<OrderDeliveryRpcQryDTO> getByOrderId(Integer orderId);

    ResponseBaseBean syncOutboundOrderFromERP(ErpDeliverOrderRequest erpDeliverOrderRequest);

    /**
     * 根据订单发货流水id
     *
     * @param id
     * @return
     */
    Response<OrderLogisticsSyncParam> queryLogisticsParamByOrderDeliveryBillId(Integer id);

    /**
     * 根据订单id找到取消订单参数 orderId
     *
     * @param orderId
     * @return
     */
    Response<OrderCancelSyncParam> queryCancelOrderParamByOrderId(Integer orderId);

    /**
     * 根据订单发货单流水id 组装同步ERP失败的参数
     *
     * @param id
     * @return
     */
    Response<OrderDeliverSyncErpParam> querySyncErpParamByOrderDeliverId(Integer id);

    /**
     * 设置ERP采购单和出库单
     *
     * @param id
     * @param outBoundNo
     *            出库单号
     * @param purchaseNo
     *            采购单号
     * @return
     */
    Response setErpPurchaseNoAndOutBoundNo(Integer id, String outBoundNo, String purchaseNo);

    /**
     * ERP->B2B销售出库单状态变更
     *
     * @param request
     */
    ResponseBaseBean changeDeliverOrderStatus(ErpDeliverOrderStatusRequest request);

    /**
     * 更新发货明细推送状态
     *
     * @param id
     * @param pushStatus
     * @return
     */
    Response updatePushStatus(Integer id, Short pushStatus);

    /**
     * 获取未同步erp订单发货单ids列表
     *
     * @return
     */
    Response<List<Integer>> getNotErpOrderDeliverBillIds(Date startTime);

    /**
     * <h2>查询订单id</h2>
     */
    Response<Integer> queryFactoryTrackingNumber(String orderNo);

}
