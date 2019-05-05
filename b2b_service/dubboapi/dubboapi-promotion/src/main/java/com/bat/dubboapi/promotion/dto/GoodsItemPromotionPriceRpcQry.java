package com.bat.dubboapi.promotion.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/25 19:59
 */
public class GoodsItemPromotionPriceRpcQry implements Serializable {
    private Integer distributorId;
    private Integer customerId;
    private Date orderTime;
    /**
     * 是否正在使用活动,1 是 0 否（只是计算）
     */
    private Short useFlag;
    private List<GoodsItemPriceRpcQry> goodsItemPrices;

    public Short getUseFlag() {
        return useFlag;
    }

    public void setUseFlag(Short useFlag) {
        this.useFlag = useFlag;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public List<GoodsItemPriceRpcQry> getGoodsItemPrices() {
        return goodsItemPrices;
    }

    public void setGoodsItemPrices(List<GoodsItemPriceRpcQry> goodsItemPrices) {
        this.goodsItemPrices = goodsItemPrices;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
}
