package com.bat.dubboapi.goods.stock.dto;

import java.io.Serializable;

public class GoodsStockFlagDTO implements Serializable {
    private static final long serialVersionUID = -8746543892658632281L;

    private Integer goodsId;
    private Integer itemId;

    private String warehouseIdArr;

    /**
     * 是否缺货 1、切货 0、不缺货
     */
    private Short underStockFlag;

    /**
     * 在途是否缺货 1、缺货 0、不缺货
     */
    private Short whetherOutOfStockInTransit;

    public Short getWhetherOutOfStockInTransit() {
        return whetherOutOfStockInTransit;
    }

    public void setWhetherOutOfStockInTransit(Short whetherOutOfStockInTransit) {
        this.whetherOutOfStockInTransit = whetherOutOfStockInTransit;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getWarehouseIdArr() {
        return warehouseIdArr;
    }

    public void setWarehouseIdArr(String warehouseIdArr) {
        this.warehouseIdArr = warehouseIdArr;
    }

    public Short getUnderStockFlag() {
        return underStockFlag;
    }

    public void setUnderStockFlag(Short underStockFlag) {
        this.underStockFlag = underStockFlag;
    }

    @Override
    public String toString() {
        return "GoodsStockFlagDTO{" + "goodsId=" + goodsId + ", itemId=" + itemId + ", warehouseIdArr='"
            + warehouseIdArr + '\'' + ", underStockFlag=" + underStockFlag + ", whetherOutOfStockInTransit="
            + whetherOutOfStockInTransit + '}';
    }
}
