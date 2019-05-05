package com.bat.dubboapi.order.order.dto;

import java.io.Serializable;
import java.util.Date;

public class OrderDeliveryCmd implements Serializable {
    /**
     * 国家id
     */
    private Integer countryId;
    /**
     * 省份id
     */
    private Integer provinceId;
    /**
     * 城市id
     */
    private Integer cityId;
    /**
     * 区域id
     */
    private Integer districtId;
    /**
     * 国家名称
     */
    private String countryName;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 区域名称
     */
    private String districtName;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 邮编
     */
    private String zipCode;
    /**
     * 收货人
     */
    private String userName;
    /**
     * 收货人手机号
     */
    private String mobile;
    /**
     * 收货人固定电话
     */
    private String phone;
    /**
     * 送货时间类型 1.工作日，2.任意时间 3.双休 4.指定日期
     */
    private Short deliveryType;
    /**
     * 送货时间
     */
    private Date deliveryTime;

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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Short getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Short deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}