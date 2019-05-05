package com.bat.dubboapi.thirdparty.erp.api;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.dto.order.OrderGoodsDetailDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * ERP接口
 */
public interface ThirdPartyOrderServiceErpRpc {

    /**
     * ERP下推采购入库单
     * 
     * @return
     */
    public Response<String> syncErpPurchase(String orderErpNo, String factoryNo, List<OrderGoodsDetailDTO> detailDTOList,
                                            String time);

    /**
     * 下推销售出库单
     * 
     * @param orderErpNo
     * @param manufactors
     * @param orderGoodsDetailDTOS
     * @param deliverTime
     * @param distributionAmount
     * @param distributionName
     * @param logisticNo
     * @param time
     * @return
     */
    Response<String> syncOutStockToERP(String orderErpNo, String manufactors,
        List<OrderGoodsDetailDTO> orderGoodsDetailDTOS, String deliverTime, BigDecimal distributionAmount,
        String distributionName, String logisticNo, String time);

    /**
     * 取消ERP订单
     * @param orderErpNo
     * @param operateType 1、作废 2、取消
     * @param cancelRemark
     * @return
     */
    Response cancelOrder(String orderErpNo, Short operateType, Integer orderId, String cancelRemark);

    Response syncOrderToErp(Integer orderId);
}
