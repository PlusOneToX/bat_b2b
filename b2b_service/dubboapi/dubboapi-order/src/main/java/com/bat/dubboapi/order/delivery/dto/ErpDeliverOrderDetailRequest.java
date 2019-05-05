package com.bat.dubboapi.order.delivery.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by apple on 2019/7/10.
 */
@Data
public class ErpDeliverOrderDetailRequest implements Serializable {

    private static final long serialVersionUID = -4446209076881586287L;

    private String orderNo;
    private String itemNo;
    private Integer num;
    private Integer alreadyNum;
    private String no;
    private Long EntryId;

    /**
     * 销售单内码id、对应orderGoods的行序号
     */
    private Integer orderEntryId;


}
