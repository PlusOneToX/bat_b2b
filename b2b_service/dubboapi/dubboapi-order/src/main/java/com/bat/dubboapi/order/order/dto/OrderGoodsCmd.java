package com.bat.dubboapi.order.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderGoodsCmd implements Serializable {
    /**
     * 订单明细序号（从1开始）
     */
    private Integer serialNumber;

    /**
     * 货品编码
     */
    private String itemCode;
    /**
     * 货品购买数量
     */
    private Integer itemCount;
    /**
     * 定制信息
     */
    private OrderGoodsDiyCmd diy;

    /**
     * 货品销售单价（如果有值，已此值为货品销售单价，没值，需通过计算获取货品销售单价）
     */
    private BigDecimal salePrice;

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public OrderGoodsDiyCmd getDiy() {
        return diy;
    }

    public void setDiy(OrderGoodsDiyCmd diy) {
        this.diy = diy;
    }
}