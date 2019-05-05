package com.bat.dubboapi.distributor.distributor.dto.data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class NextScalePriceRpcDTO implements Serializable {
    private Integer id;

    private Short arithmeticType;

    private BigDecimal arithmeticNum;

    private Short specialFlag;

    private List<NextScalePriceSpecialRpcDTO> nextScalePriceSpecials;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Short getSpecialFlag() {
        return specialFlag;
    }

    public void setSpecialFlag(Short specialFlag) {
        this.specialFlag = specialFlag;
    }

    public List<NextScalePriceSpecialRpcDTO> getNextScalePriceSpecials() {
        return nextScalePriceSpecials;
    }

    public void setNextScalePriceSpecials(List<NextScalePriceSpecialRpcDTO> nextScalePriceSpecials) {
        this.nextScalePriceSpecials = nextScalePriceSpecials;
    }
}