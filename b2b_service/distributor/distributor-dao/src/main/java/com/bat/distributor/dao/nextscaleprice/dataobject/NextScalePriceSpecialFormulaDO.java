package com.bat.distributor.dao.nextscaleprice.dataobject;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class NextScalePriceSpecialFormulaDO implements Serializable {
    private Integer nextScalePriceId;
    private Integer brandId;

    private Integer categoryId;

    private Short arithmeticType;

    private BigDecimal arithmeticNum;

}