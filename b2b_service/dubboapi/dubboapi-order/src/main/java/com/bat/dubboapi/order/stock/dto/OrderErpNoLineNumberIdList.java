package com.bat.dubboapi.order.stock.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderErpNoLineNumberIdList implements Serializable {

    private static final long serialVersionUID = 2917383220396588771L;
    /**
     * ERP单号
     */
    private String orderErpNo;

    /**
     * 行序号id列表（ERP传过来的是销售订单明细内码）、对应ERP和B2B的order_goos行序号
     */
    private List<Integer> lineNumberIdList;
}
