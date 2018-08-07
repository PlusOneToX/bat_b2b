package com.bat.warehouse.api.erp.order.dto;

import lombok.Data;

@Data
public class ErpDeliverOrderDetailCmd {

    private String orderNo;
    private String itemNo;
    private Integer num;
    private Integer alreadyNum;

    //仓库编码
    private String no;
    private Long EntryId;


}
