package com.bat.thirdparty.erp.api.response.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceInfo {
    @JsonProperty("FCUSTID")
    private String FCUSTID;
    @JsonProperty("FZHYE")
    private BigDecimal FZHYE;// 账户余额

    public String getFCUSTID() {
        return FCUSTID;
    }

    public void setFCUSTID(String FCUSTID) {
        this.FCUSTID = FCUSTID;
    }

    public BigDecimal getFZHYE() {
        return FZHYE;
    }

    public void setFZHYE(BigDecimal FZHYE) {
        this.FZHYE = FZHYE;
    }

    @Override
    public String toString() {
        return "BalanceInfo{" + "FCUSTID='" + FCUSTID + '\'' + ", FZHYE=" + FZHYE + '}';
    }
}
