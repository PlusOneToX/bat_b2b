package com.bat.distributor.dao.nextscaleprice.dataobject;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class NextScalePriceSpecialDO {
    private Integer id;

    private Integer nextScalePriceId;

    private Short arithmeticType;

    private BigDecimal arithmeticNum;
}