package com.bat.dubboapi.warehouse.stock.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsItemCountDTO implements Serializable {

    private static final long serialVersionUID = -2546647722295939906L;
    /**
     * 货品id
     */
    private Integer itemId;

    /**
     * 在库下单数量
     */
    private Integer inStockCount;

    /**
     * 在途下单数量
     */
    private Integer onWayCount;
    /**
     * 是否支持超卖 true、支持 false 不支持
     */
    private Boolean supportOversoldFlag=false;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getInStockCount() {
        return inStockCount;
    }

    public void setInStockCount(Integer inStockCount) {
        this.inStockCount = inStockCount;
    }

    public Integer getOnWayCount() {
        return onWayCount;
    }

    public void setOnWayCount(Integer onWayCount) {
        this.onWayCount = onWayCount;
    }

    public Boolean getSupportOversoldFlag() {
        return supportOversoldFlag;
    }

    public void setSupportOversoldFlag(Boolean supportOversoldFlag) {
        this.supportOversoldFlag = supportOversoldFlag;
    }
}
