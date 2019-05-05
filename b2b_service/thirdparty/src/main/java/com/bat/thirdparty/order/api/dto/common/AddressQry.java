package com.bat.thirdparty.order.api.dto.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddressQry {

    @ApiModelProperty(value = "省名称")
    @NotBlank(message = "T_ORDER_ADDRESS_PROVINCE_NULL")
    private String province;

    @ApiModelProperty(value = "城市名称")
    @NotBlank(message = "T_ORDER_ADDRESS_CITY_NULL")
    private String city;

    @ApiModelProperty(value = "区名称")
    @NotBlank(message = "T_ORDER_ADDRESS_AREA_NULL")
    private String area;

    @ApiModelProperty(value = "地址详情（不要拼上省市区名称）")
    @NotBlank(message = "T_ORDER_ADDRESS_DETAIL_NULL")
    private String detail;


    public AddressQry(String province,  String city,  String area,  String detail) {
        this.province = province;
        this.city = city;
        this.area = area;
        this.detail = detail;
    }
}