package com.bat.dubboapi.platform.tenant.dto.data;

import java.io.Serializable;
import java.util.Date;

public class PlatformTenantDiyFactoryRpcDTO implements Serializable {
    private Integer id;

    private Integer tenantId;

    private String tenantNo;

    private String factoryNo;

    private String factoryName;

    private String appId;

    private String appKey;

    private String appSecret;

    private String orderAddUrl;

    private String orderCancelUrl;

    private String supplierNo;

    private String warehouseNo;

    private String shopName;

    private Date createTime;

    private Date updateTime;

    private String configOther;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantNo() {
        return tenantNo;
    }

    public void setTenantNo(String tenantNo) {
        this.tenantNo = tenantNo;
    }

    public String getFactoryNo() {
        return factoryNo;
    }

    public void setFactoryNo(String factoryNo) {
        this.factoryNo = factoryNo;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getOrderAddUrl() {
        return orderAddUrl;
    }

    public void setOrderAddUrl(String orderAddUrl) {
        this.orderAddUrl = orderAddUrl;
    }

    public String getOrderCancelUrl() {
        return orderCancelUrl;
    }

    public void setOrderCancelUrl(String orderCancelUrl) {
        this.orderCancelUrl = orderCancelUrl;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getConfigOther() {
        return configOther;
    }

    public void setConfigOther(String configOther) {
        this.configOther = configOther;
    }
}