package com.bat.distributor.dao.distributor.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class DistributorAddressDO {
    private Integer id;

    private String userName;

    private String phone;

    private Integer distributorId;

    private Integer countryId;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

    private String address;

    private String zipCode;

    private Short addressType;

    private Short defaultFlag;

    private Date createTime;

    private Date updateTime;
}