package com.bat.dubboapi.warehouse.stock.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ErpStockChangeCmd implements Serializable {

    private static final long serialVersionUID = 5315944869156016345L;
    /**
     * ERP流水id
     */
    private String billNo;
    /**
     * 库存单据类型
     */
    private String stockBillType;
    /**
     * 库存变化类型 1.增加，2.减少（出库）传负数 （该属性没有ERP没有传过来）
     */
    private Short changeType;

    private List<ErpItemStockChangeCmd> stockBillDetails;

}
