package com.bat.dubboapi.order.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ErpOrderChangeDetailCmd implements Serializable {
    /**
     * 物料编号
     */
    private String itemNo;
    /**
     * 含税价
     */
    private BigDecimal itemTaxPrice;
    private String itemName;
    private Integer num;
    /**
     * 是否赠品
     */
    private Boolean isFree;
    /**
     * 变更单明细内码(erp变更单明细表主键)
     */
    private Integer EntryId;
    /**
     * 销售订单明细内码(erp销售订单明细表主键)
     */
    private Integer itemOrderId;
    /**
     * 销售订单明细变更类型：A 增加 B 修改 D 删除
     */
    private String itemFChangeType;
}
