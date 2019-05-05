package com.bat.dubboapi.system.logistics.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/27 20:17
 */
public class LogisticsCalculationRpcQry implements Serializable {

    private Integer logisticsId;

    private Integer countryId;

    private Integer provinceId;

    private Integer cityId;

    private BigDecimal price;

    private Double weight;

    private Double volume;

    public Integer getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Integer logisticsId) {
        this.logisticsId = logisticsId;
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

    @Override
    public String toString() {
        return "LogisticsCalculationRpcQry{" + "logisticsId=" + logisticsId + ", countryId=" + countryId
            + ", provinceId=" + provinceId + ", cityId=" + cityId + ", price=" + price + ", weight=" + weight
            + ", volume=" + volume + '}';
    }
}