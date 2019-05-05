package com.bat.dubboapi.thirdparty.erp.dto.order.purchase;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErpPurchaseOrderOutboundCmd implements Serializable {

    private static final long serialVersionUID = 418796161671756280L;
    /**
     * 采购销售单
     */
    private DiyPurchaseOrderDTO diyPurchaseOrderDTO;

    /**
     * 出库单
     */
    private DiyOutboundSyncErpCmd diyOutboundSyncErpCmd;
}
