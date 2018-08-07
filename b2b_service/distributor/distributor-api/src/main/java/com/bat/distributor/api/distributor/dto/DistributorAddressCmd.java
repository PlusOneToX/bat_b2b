package com.bat.distributor.api.distributor.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商地址(包括收货地址)信息")
public class DistributorAddressCmd {

    private Integer id;
    @ApiModelProperty(value = "收货人姓名", required = false, example = "bat")
    private String userName;
    @NotNull(message = "P_DISTRIBUTOR_COUNTRY_ID_NULL")
    @ApiModelProperty(value = "国家id", required = true, example = "1234")
    private Integer countryId;
    @ApiModelProperty(value = "省份id(国家为中国时必填)", required = false, example = "1234")
    private Integer provinceId;
    @ApiModelProperty(value = "城市id(国家为中国时必填)", required = false, example = "1234")
    private Integer cityId;
    @ApiModelProperty(value = "区id", required = false, example = "1234")
    private Integer districtId;
    @ApiModelProperty(value = "地址(当地址类型为2时必填)", required = false, example = "地址")
    private String address;
    @ApiModelProperty(value = "邮编", required = false, example = "123455")
    private String zipCode;
    @NotNull(message = "P_DISTRIBUTOR_ADDRESS_TYPE_NULL")
    @ApiModelProperty(value = "地址类型 1.公司地址 2.收货地址(添加分销商都传1)", required = true, example = "1")
    private Short addressType;
    @ApiModelProperty(value = "是否默认收货地址 0.否 1.是 (当地址类型为2时必填)", required = false, example = "0")
    private Short defaultFlag;
}
