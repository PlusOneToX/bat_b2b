package com.bat.dubboapi.warehouse.stock.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsItemErpDTO implements Serializable {

    private static final long serialVersionUID = 2177125457672126036L;
    /**
     * 货品id
     */
    private Integer itemId;


    /**
     * 货品的itemErpId
     */
    private Integer itemErpId;


    private String itemName;


    private String itemCode;
}
