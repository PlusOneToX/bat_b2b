package com.bat.dubboapi.flexible.model.dto;

import java.io.Serializable;

public class ErpGoodsCustomInfoBomCmd implements Serializable {

    private static final long serialVersionUID = 6204493946926263561L;
    /**
     * BomCode编码
     */
    private String bomCode;


    /**
     * 材料编码
     */
    private String itemCode;


    /**
     * 操作类型 1、上架 0、下架
     */
    private Byte operateType;

    /**
     * 是否缺货 1、缺货 0、不缺货
     */
    private Short stockOutStatus;



    public String getBomCode() {
        return bomCode;
    }

    public void setBomCode(String bomCode) {
        this.bomCode = bomCode;
    }

    public Byte getOperateType() {
        return operateType;
    }

    public void setOperateType(Byte operateType) {
        this.operateType = operateType;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Short getStockOutStatus() {
        return stockOutStatus;
    }

    public void setStockOutStatus(Short stockOutStatus) {
        this.stockOutStatus = stockOutStatus;
    }
}
