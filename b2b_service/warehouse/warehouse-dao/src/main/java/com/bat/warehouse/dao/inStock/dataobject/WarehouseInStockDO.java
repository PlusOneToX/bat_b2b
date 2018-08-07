package com.bat.warehouse.dao.inStock.dataobject;

import java.io.Serializable;
import java.util.Date;

public class WarehouseInStockDO implements Serializable {
    private static final long serialVersionUID = 9209371796686684928L;
    private Integer id;

    private Integer warehouseId;

    private Integer goodsId;

    private Integer itemId;

    private Integer itemErpId;

    private Integer numInWarehouse;

    private Integer numOnWay;

    private Integer numInWarehouseLock;

    private Integer numOnWayLock;

    private Integer numReserved;

    private Integer erpNumInWarehouse;

    private Integer erpNumLock;

    private Date createTime;

    private Date updateTime;

    private String itemCode;

    private String itemName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
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

    public Integer getItemErpId() {
        return itemErpId;
    }

    public void setItemErpId(Integer itemErpId) {
        this.itemErpId = itemErpId;
    }

    public Integer getNumInWarehouse() {
        return numInWarehouse;
    }

    public void setNumInWarehouse(Integer numInWarehouse) {
        this.numInWarehouse = numInWarehouse;
    }

    public Integer getNumOnWay() {
        return numOnWay;
    }

    public void setNumOnWay(Integer numOnWay) {
        this.numOnWay = numOnWay;
    }

    public Integer getNumInWarehouseLock() {
        return numInWarehouseLock;
    }

    public void setNumInWarehouseLock(Integer numInWarehouseLock) {
        this.numInWarehouseLock = numInWarehouseLock;
    }

    public Integer getNumOnWayLock() {
        return numOnWayLock;
    }

    public void setNumOnWayLock(Integer numOnWayLock) {
        this.numOnWayLock = numOnWayLock;
    }

    public Integer getNumReserved() {
        return numReserved;
    }

    public void setNumReserved(Integer numReserved) {
        this.numReserved = numReserved;
    }

    public Integer getErpNumInWarehouse() {
        return erpNumInWarehouse;
    }

    public void setErpNumInWarehouse(Integer erpNumInWarehouse) {
        this.erpNumInWarehouse = erpNumInWarehouse;
    }

    public Integer getErpNumLock() {
        return erpNumLock;
    }

    public void setErpNumLock(Integer erpNumLock) {
        this.erpNumLock = erpNumLock;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode == null ? null : itemCode.trim();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }
}