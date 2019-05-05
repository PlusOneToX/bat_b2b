package com.bat.dubboapi.order.order.dto;

import java.io.Serializable;

public class OrderLogisticsCmd implements Serializable {
    /**
     * 配送方式类型：1 普通商品（标品） 2 定制工厂
     */
    private Short logisticsType;
    /**
     * 生产商 YC云创 MK麦客 LHW联辉王（当配送方式类型为2时必填）
     */
    private String manufactors;
    /**
     * 配送方式id
     */
    private Integer logisticsId;
    /**
     * 配送方式名称
     */
    private String logisticsName;

    public Short getLogisticsType() {
        return logisticsType;
    }

    public void setLogisticsType(Short logisticsType) {
        this.logisticsType = logisticsType;
    }

    public String getManufactors() {
        return manufactors;
    }

    public void setManufactors(String manufactors) {
        this.manufactors = manufactors;
    }

    public Integer getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Integer logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }
}