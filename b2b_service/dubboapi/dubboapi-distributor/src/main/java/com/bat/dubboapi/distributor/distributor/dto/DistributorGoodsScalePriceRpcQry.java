package com.bat.dubboapi.distributor.distributor.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 19:49
 */
public class DistributorGoodsScalePriceRpcQry implements Serializable {

    private Integer distributorId;

    private List<Integer> goodsItemIds;

    private List<GoodsScalePriceRpcQry> goodsScalePrices;

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public List<GoodsScalePriceRpcQry> getGoodsScalePrices() {
        return goodsScalePrices;
    }

    public void setGoodsScalePrices(List<GoodsScalePriceRpcQry> goodsScalePrices) {
        this.goodsScalePrices = goodsScalePrices;
    }

    public List<Integer> getGoodsItemIds() {
        return goodsItemIds;
    }

    public void setGoodsItemIds(List<Integer> goodsItemIds) {
        this.goodsItemIds = goodsItemIds;
    }
}
