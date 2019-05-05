package com.bat.dubboapi.thirdparty.erp.dto.warehouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class InquiryInfo implements Serializable {
    private static final long serialVersionUID = -1480625405366192242L;
    @JsonProperty(value = "FMATERIALID")
    private String FMATERIALID; //物料编码、erpid、非80码
    @JsonProperty(value = "FBASEQTY")
    private Long FBASEQTY;    //库存量
    @JsonProperty(value = "LockedQuantity")
    private Long LockedQuantity; //已锁库数量
    @JsonProperty(value = "QuantityInTransit")
    private Long QuantityInTransit; //在途数量
    @JsonProperty(value = "PREDICTQTY")
    private Long PREDICTQTY;//预计可发量 库存量-已锁库数量-预计出

    /**
     * 仓库id、B2B设置库存使用、非ERP返回
     */
    private Integer warehouseId;


    @Override
    public String toString() {
        return "物料编码:" + FMATERIALID + ",库存量:" + FBASEQTY + ",已锁库数量:" + LockedQuantity + ",在途数量:" + QuantityInTransit + ",预计可发量:" + PREDICTQTY;
    }
}
