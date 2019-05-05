package com.bat.thirdparty.order.api;

import com.bat.thirdparty.common.base.Response;

public interface AdminOrderServiceI {

    /**
     * 同步销售单到ERP
     *
     * @param orderId
     * @return
     */
    Response syncOrderToERP(Integer orderId);

    /**
     * 同步销售单到工厂
     *
     * @param orderId
     * @return
     */
    Response syncOrderToFactory(Integer orderId);

    /**
     * 同步物流到第三方
     *
     * @param id
     * @return
     */
    Response syncLogisticsToThird(Integer id);

    /**
     * 同步柔性发货单到ERP(销售单、采购单)
     *
     * @param id
     * @return
     */
    Response syncDiyDeliveryOrderToERP(Integer id);

    /**
     * 手动生成标签
     *
     * @param orderGoodsDiyId
     * @return
     */
    com.bat.dubboapi.order.common.Response createLable(Integer orderGoodsDiyId);

    /**
     * 同步ERP快递单号
     *
     * @return
     */
    Response syncErpExpressNo();

    /**
     * 批量同步erp订单
     */
    void orderSyncToErp(String startTime);

    /**
     * 批量同步erp柔性订单出库单
     */
    void orderPurchaseAndOutboundSyncToErp(String startTime);

    void createOrderLatelyNullLabel();

    /**
     * 定时器 自动关闭订单
     * <p>
     * 主要针对 款到发货+直发订单，超3天未出货，自动关闭订单
     */
    void autoCloseOrderJobHandler();

    /**
     * <h2>按订单 ID 同步物流</h2>
     * 
     * @param id
     * @return
     */
    Response synchronizedLogisticsByOrderID(Integer id);

    /**
     * 根据工厂同步订单物流信息
     * @param factoryCode
     */
    Response synchronizedLogisticsByFactoryCode(String factoryCode,String startTime);
}
