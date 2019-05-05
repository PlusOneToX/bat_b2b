package com.bat.dubboapi.thirdparty.erp.dto.financial.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceInfoDTO implements Serializable {
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
        return "BalanceInfoDTO{" + "FCUSTID='" + FCUSTID + '\'' + ", FZHYE=" + FZHYE + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BalanceInfoDTO infoDTO = (BalanceInfoDTO)o;
        return Objects.equals(FCUSTID, infoDTO.FCUSTID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(FCUSTID);
    }
}
