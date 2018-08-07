package com.bat.order.service.common.data.dto;

import java.util.Date;

import lombok.Data;

@Data
public class OrderDeliveryDTO {
    /**
     * 国家id
     */
    private Integer countryId;
    /**
     * 省份id(不存在可以填0)
     */
    private Integer provinceId;
    /**
     * 城市id(不存在可以填0)
     */
    private Integer cityId;
    /**
     * 区域id(不存在可以填0)
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
}