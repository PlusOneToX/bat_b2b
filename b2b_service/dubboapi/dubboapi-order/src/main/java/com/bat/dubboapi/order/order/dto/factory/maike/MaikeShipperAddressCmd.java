package com.bat.dubboapi.order.order.dto.factory.maike;


import java.io.Serializable;

public class MaikeShipperAddressCmd implements Serializable {

    private static final long serialVersionUID = 3653586297994233202L;
    /**
     * 收货人
     */
    private String consignee;


    /**
     * 收货电话
     */
    private String mobile;

    /**
     * 省
     */
    private String province;


    /**
     * 城市
     */
    private String city;


    /**
     * 东城区
     */
    private String district;

    /**
     * 地址
     */
    private String address;

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    
}