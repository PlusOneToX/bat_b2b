package com.bat.order.api.order.dto.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderAddress {

    @ApiModelProperty(value = "收货地址id")
    private Integer addressId;

    @ApiModelProperty(value = "国家id")
    @NotNull(message = "ADDRESS_COUNTRY_ID_NULL")
    private Integer countryId;

    @ApiModelProperty(value = "省id")
    @NotNull(message = "ADDRESS_PROVINCE_ID_NULL")
    private Integer provinceId;

    @ApiModelProperty(value = "城市id")
    @NotNull(message = "ADDRESS_CITY_ID_NULL")
    private Integer cityId;

    @ApiModelProperty(value = "区id")
    @NotNull(message = "ADDRESS_DISTRICT_ID_NULL")
    private Integer districtId;

    @ApiModelProperty(value = "国家")
    @NotNull(message = "ADDRESS_COUNTRY_NAME_NULL")
    private String countryName;

    @ApiModelProperty(value = "省")
    @NotNull(message = "ADDRESS_PROVINCE_NAME_NULL")
    private String provinceName;

    @ApiModelProperty(value = "城市")
    @NotNull(message = "ADDRESS_CITY_NAME_NULL")
    private String cityName;

    @ApiModelProperty(value = "区名称")
    @NotNull(message = "ADDRESS_DISTRICT_NAME_NULL")
    private String districtName;

    @ApiModelProperty(value = "地址")
    @NotNull(message = "ADDRESS_ADDRESS_NULL")
    private String address;

    @ApiModelProperty(value = "邮政编码")
    @NotNull(message = "ADDRESS_ZIP_CODE_NULL")
    private String zipCode;

    @ApiModelProperty(value = "移动电话")
    @NotNull(message = "ADDRESS_MOBILE_NULL")
    private String mobile;

    @ApiModelProperty(value = "固定电话")
    private String phone;

}
