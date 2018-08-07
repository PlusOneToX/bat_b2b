package com.bat.system.dao.logistics.dataobject;

import java.util.Date;

public class LogisticsAreaDO {
    private Integer id;

    private Integer logisticsId;

    private Double firstWeightCost;

    private Double firstVolumeCost;

    private Double additionalWeightCost;

    private Double additionalVolumeCost;

    private Short defaultFlag;

    private Short formulaFlag;

    private String formula;

    private Date createTime;

    private Date updateTime;

    private String groupId;

    private Integer countryId;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Integer logisticsId) {
        this.logisticsId = logisticsId;
    }

    public Double getFirstWeightCost() {
        return firstWeightCost;
    }

    public void setFirstWeightCost(Double firstWeightCost) {
        this.firstWeightCost = firstWeightCost;
    }

    public Double getFirstVolumeCost() {
        return firstVolumeCost;
    }

    public void setFirstVolumeCost(Double firstVolumeCost) {
        this.firstVolumeCost = firstVolumeCost;
    }

    public Double getAdditionalWeightCost() {
        return additionalWeightCost;
    }

    public void setAdditionalWeightCost(Double additionalWeightCost) {
        this.additionalWeightCost = additionalWeightCost;
    }

    public Double getAdditionalVolumeCost() {
        return additionalVolumeCost;
    }

    public void setAdditionalVolumeCost(Double additionalVolumeCost) {
        this.additionalVolumeCost = additionalVolumeCost;
    }

    public Short getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Short defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public Short getFormulaFlag() {
        return formulaFlag;
    }

    public void setFormulaFlag(Short formulaFlag) {
        this.formulaFlag = formulaFlag;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula == null ? null : formula.trim();
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    @Override
    public String toString() {
        return "LogisticsAreaDO{" + "id=" + id + ", logisticsId=" + logisticsId + ", firstWeightCost=" + firstWeightCost
            + ", firstVolumeCost=" + firstVolumeCost + ", additionalWeightCost=" + additionalWeightCost
            + ", additionalVolumeCost=" + additionalVolumeCost + ", defaultFlag=" + defaultFlag + ", formulaFlag="
            + formulaFlag + ", formula='" + formula + '\'' + ", createTime=" + createTime + ", updateTime=" + updateTime
            + ", groupId='" + groupId + '\'' + ", countryId=" + countryId + ", provinceId=" + provinceId + ", cityId="
            + cityId + ", districtId=" + districtId + '}';
    }
}