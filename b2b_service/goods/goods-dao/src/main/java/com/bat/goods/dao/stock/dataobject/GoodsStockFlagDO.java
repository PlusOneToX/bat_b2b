package com.bat.goods.dao.stock.dataobject;

import java.io.Serializable;
import java.util.Date;

public class GoodsStockFlagDO implements Serializable {
    private static final long serialVersionUID = -334528036412550243L;
    private Integer id;

    private Integer itemId;

    private String warehouseIdArr;

    private Short underStockFlag;

    private Date updateTime;

    private Integer goodsId;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}