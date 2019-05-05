package com.bat.dubboapi.order.delivery.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDeliverSyncErpParam implements Serializable {


    private static final long serialVersionUID = -5811835760221440921L;

    private String orderErpNo;

    private String manufactors;

    private List<OrderGoodsDetailCountDTO> orderGoodsDetailDTOS;


    private String deliverTime;


    private BigDecimal distributionAmount;

    private String distributionName;


    private String logisticNo;

    private String time;

    /**
     * 是否需要同步ERP出库单
     */
    private Boolean syncOutbountFlag=true;

    /**
     * 是否需要同步ERP采购单
     */
    private Boolean syncPurchaseFlag=true;

}
