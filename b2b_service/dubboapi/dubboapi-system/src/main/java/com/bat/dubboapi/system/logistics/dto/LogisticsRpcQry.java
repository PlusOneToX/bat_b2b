package com.bat.dubboapi.system.logistics.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/27 20:17
 */
public class LogisticsRpcQry implements Serializable {

    /**
     * 物流id
     */
    private Integer logisticsId;

    /**
     * 适用范围 1.普通商品，2.定制商品 3 普通商品和定制商品
     */
    private String useRange;

    /**
     * 物流名称
     */
    private String name;

    /**
     * erp物流id
     */
    private String logisticsErpId;

    /**
     * 快递鸟快递公司编码
     */
    private String logisticsKdnCode;

    /**
     * 快递鸟快递公司名称
     */
    private String logisticsKdnName;

    /**
     * 工厂配送方式编号
     */
    private String logisticsFactoryId;

    /**
     * 生产商 YC.云创 LHW.联辉王
     */
    private String manufactors;

    /**
     * 国家id
     */
    private Integer countryId;

    /**
     * 省id
     */
    private Integer provinceId;

    /**
     * 市id
     */
    private Integer cityId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 重量克
     */
    private Double weight;

    /**
     * 体积 立方毫米
     */
    private Double volume;

    /**
     * 分销商等级
     */
    private Integer gradeId;
    /**
     * 部门
     */
    private Integer departmentId;
    /**
     * 业务员
     */
    private Integer userId;
    /**
     * 分销商id
     */
    private Integer distributorId;

    private Short enable;

    public Integer getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Integer logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getUseRange() {
        return useRange;
    }

    public void setUseRange(String useRange) {
        this.useRange = useRange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogisticsErpId() {
        return logisticsErpId;
    }

    public void setLogisticsErpId(String logisticsErpId) {
        this.logisticsErpId = logisticsErpId;
    }

    public String getLogisticsKdnCode() {
        return logisticsKdnCode;
    }

    public void setLogisticsKdnCode(String logisticsKdnCode) {
        this.logisticsKdnCode = logisticsKdnCode;
    }

    public String getLogisticsKdnName() {
        return logisticsKdnName;
    }

    public void setLogisticsKdnName(String logisticsKdnName) {
        this.logisticsKdnName = logisticsKdnName;
    }

    public String getLogisticsFactoryId() {
        return logisticsFactoryId;
    }

    public void setLogisticsFactoryId(String logisticsFactoryId) {
        this.logisticsFactoryId = logisticsFactoryId;
    }

    public String getManufactors() {
        return manufactors;
    }

    public void setManufactors(String manufactors) {
        this.manufactors = manufactors;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Short getEnable() {
        return enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "LogisticsRpcQry{" + "logisticsId=" + logisticsId + ", useRange='" + useRange + '\'' + ", name='" + name
            + '\'' + ", logisticsErpId='" + logisticsErpId + '\'' + ", logisticsKdnCode='" + logisticsKdnCode + '\''
            + ", logisticsKdnName='" + logisticsKdnName + '\'' + ", logisticsFactoryId='" + logisticsFactoryId + '\''
            + ", manufactors='" + manufactors + '\'' + ", countryId=" + countryId + ", provinceId=" + provinceId
            + ", cityId=" + cityId + ", price=" + price + ", weight=" + weight + ", volume=" + volume + ", gradeId="
            + gradeId + ", departmentId=" + departmentId + ", userId=" + userId + ", distributorId=" + distributorId
            + ", enable=" + enable + '}';
    }
}