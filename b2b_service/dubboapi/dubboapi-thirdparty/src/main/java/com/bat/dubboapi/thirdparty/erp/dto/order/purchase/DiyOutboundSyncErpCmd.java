package com.bat.dubboapi.thirdparty.erp.dto.order.purchase;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DiyOutboundSyncErpCmd extends DiyPurchaseOrderDTO implements Serializable {
    private static final long serialVersionUID = -5279162344504941098L;

    private String deliverTime;

    private BigDecimal distributionAmount;


    private String distributionName;


    private String logisticsNo;

}
