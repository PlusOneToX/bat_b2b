package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class NextScalePriceSpecialRpcDTO implements Serializable {
    private Integer brandId;

    private Integer categoryId;

    private Short arithmeticType;

    private BigDecimal arithmeticNum;

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Short getArithmeticType() {
        return arithmeticType;
    }

    public void setArithmeticType(Short arithmeticType) {
        this.arithmeticType = arithmeticType;
    }

    public BigDecimal getArithmeticNum() {
        return arithmeticNum;
    }

    public void setArithmeticNum(BigDecimal arithmeticNum) {
        this.arithmeticNum = arithmeticNum;
    }
}