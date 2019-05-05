package com.bat.dubboapi.order.stock.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class OrderErpNoLineNumberList implements Serializable {

    private static final long serialVersionUID = 2917383220396588771L;
    /**
     * ERP单号
     */
    private String orderErpNo;

    /**
     * <ERP订单行序号，行数量>
     */
    private Map<Integer, Integer> lineNumberMap;
}
