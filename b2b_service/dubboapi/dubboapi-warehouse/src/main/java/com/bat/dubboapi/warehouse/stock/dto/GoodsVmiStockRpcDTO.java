package com.bat.dubboapi.warehouse.stock.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GoodsVmiStockRpcDTO implements Serializable {
    private static final long serialVersionUID = 8905862467716234197L;
    private Integer id;

    private Integer goodsId;

    private Integer itemId;

    private Integer itemErpId;

    private Integer numVmi;

    private Integer numLock;

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

    public Integer getNumVmi() {
        return numVmi;
    }

    public void setNumVmi(Integer numVmi) {
        this.numVmi = numVmi;
    }

    public Integer getNumLock() {
        return numLock;
    }

    public void setNumLock(Integer numLock) {
        this.numLock = numLock;
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