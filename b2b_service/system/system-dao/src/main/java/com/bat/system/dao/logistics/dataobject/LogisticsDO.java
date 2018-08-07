package com.bat.system.dao.logistics.dataobject;

import java.util.Date;

public class LogisticsDO {
    private Integer id;

    private String name;

    private Integer sort;

    private Short enable;

    private String description;

    private String logisticsErpId;

    private Short billingMethod;

    private Double firstWeight;

    private Double firstVolume;

    private Double additionalWeight;

    private Double additionalVolume;

    private Double minWeight;

    private Double minVolume;

    private Double maxWeight;

    private Double maxVolume;

    private Double minCost;

    private Short useRange;

    private String logisticsFactoryId;

    private String logisticsKdnCode;

    private String logisticsKdnName;

    private Short appointAreaFlag;

    private Short distributorScope;

    private String manufactors;

    private String website;

    private String materialId;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Short getEnable() {
        return enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getLogisticsErpId() {
        return logisticsErpId;
    }

    public void setLogisticsErpId(String logisticsErpId) {
        this.logisticsErpId = logisticsErpId == null ? null : logisticsErpId.trim();
    }

    public Short getBillingMethod() {
        return billingMethod;
    }

    public void setBillingMethod(Short billingMethod) {
        this.billingMethod = billingMethod;
    }

    public Double getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Double firstWeight) {
        this.firstWeight = firstWeight;
    }

    public Double getFirstVolume() {
        return firstVolume;
    }

    public void setFirstVolume(Double firstVolume) {
        this.firstVolume = firstVolume;
    }

    public Double getAdditionalWeight() {
        return additionalWeight;
    }

    public void setAdditionalWeight(Double additionalWeight) {
        this.additionalWeight = additionalWeight;
    }

    public Double getAdditionalVolume() {
        return additionalVolume;
    }

    public void setAdditionalVolume(Double additionalVolume) {
        this.additionalVolume = additionalVolume;
    }

    public Double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Double minWeight) {
        this.minWeight = minWeight;
    }

    public Double getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(Double minVolume) {
        this.minVolume = minVolume;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Double getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(Double maxVolume) {
        this.maxVolume = maxVolume;
    }

    public Double getMinCost() {
        return minCost;
    }

    public void setMinCost(Double minCost) {
        this.minCost = minCost;
    }

    public Short getUseRange() {
        return useRange;
    }

    public void setUseRange(Short useRange) {
        this.useRange = useRange;
    }

    public String getLogisticsFactoryId() {
        return logisticsFactoryId;
    }

    public void setLogisticsFactoryId(String logisticsFactoryId) {
        this.logisticsFactoryId = logisticsFactoryId == null ? null : logisticsFactoryId.trim();
    }

    public String getLogisticsKdnCode() {
        return logisticsKdnCode;
    }

    public void setLogisticsKdnCode(String logisticsKdnCode) {
        this.logisticsKdnCode = logisticsKdnCode == null ? null : logisticsKdnCode.trim();
    }

    public String getLogisticsKdnName() {
        return logisticsKdnName;
    }

    public void setLogisticsKdnName(String logisticsKdnName) {
        this.logisticsKdnName = logisticsKdnName == null ? null : logisticsKdnName.trim();
    }

    public Short getAppointAreaFlag() {
        return appointAreaFlag;
    }

    public void setAppointAreaFlag(Short appointAreaFlag) {
        this.appointAreaFlag = appointAreaFlag;
    }

    public Short getDistributorScope() {
        return distributorScope;
    }

    public void setDistributorScope(Short distributorScope) {
        this.distributorScope = distributorScope;
    }

    public String getManufactors() {
        return manufactors;
    }

    public void setManufactors(String manufactors) {
        this.manufactors = manufactors == null ? null : manufactors.trim();
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website == null ? null : website.trim();
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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

    @Override
    public String toString() {
        return "LogisticsDO{" + "id=" + id + ", name='" + name + '\'' + ", sort=" + sort + ", enable=" + enable
            + ", description='" + description + '\'' + ", logisticsErpId='" + logisticsErpId + '\'' + ", billingMethod="
            + billingMethod + ", firstWeight=" + firstWeight + ", firstVolume=" + firstVolume + ", additionalWeight="
            + additionalWeight + ", additionalVolume=" + additionalVolume + ", minWeight=" + minWeight + ", minVolume="
            + minVolume + ", maxWeight=" + maxWeight + ", maxVolume=" + maxVolume + ", minCost=" + minCost
            + ", useRange=" + useRange + ", logisticsFactoryId='" + logisticsFactoryId + '\'' + ", logisticsKdnCode='"
            + logisticsKdnCode + '\'' + ", logisticsKdnName='" + logisticsKdnName + '\'' + ", appointAreaFlag="
            + appointAreaFlag + ", distributorScope=" + distributorScope + ", manufactors='" + manufactors + '\''
            + ", website='" + website + '\'' + ", materialId=" + materialId + ", createTime=" + createTime
            + ", updateTime=" + updateTime + '}';
    }
}