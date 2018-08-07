package com.bat.distributor.dao.nextscaleprice.dataobject;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class NextScalePriceFormulaDO {
    private Integer id;

    private Integer distributorId;

    private Short arithmeticType;

    private BigDecimal arithmeticNum;

    private Short specialFlag;

    private Short openFlag;

}