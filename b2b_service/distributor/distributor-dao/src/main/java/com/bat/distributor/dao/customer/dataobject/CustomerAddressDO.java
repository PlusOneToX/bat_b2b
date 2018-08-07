package com.bat.distributor.dao.customer.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class CustomerAddressDO {
    private Integer id;

    private String userName;

    private String phone;

    private Integer customerId;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

    private String provinceName;

    private String cityName;

    private String districtName;

    private String address;

    private String zipCode;

    private Short defaultFlag;

    private Date createTime;

    private Date updateTime;

}