package com.bat.dubboapi.order.delivery.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderDeliveryRpcQryDTO implements Serializable {
    private static final long serialVersionUID = -3323899669609416908L;
    private Integer id;

    private Integer orderId;

    private Integer distributionId;

    private String distributionName;

    private String logisticsErpId;

    private Integer countryId;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

    private String countryName;

    private String provinceName;

    private String cityName;

    private String districtName;

    private String address;

    private String zipCode;

    private String userName;

    private String mobile;

    private String phone;

    private Short deliveryType;

    private Date deliveryTime;

    private Date createTime;

    private Date updateTime;


}