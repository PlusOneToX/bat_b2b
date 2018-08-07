package com.bat.order.api.order.dto.customer;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "收货地址信息")
public class OrderDeliveryCmd {
    @NotNull(message = "P_ORDER_COUNTRY_ID_NULL")
    @ApiModelProperty(value = "国家id", required = true, example = "123")
    private Integer countryId;
    @NotNull(message = "P_ORDER_PROVINCE_ID_NULL")
    @ApiModelProperty(value = "省份id(不存在可以填0)", required = false, example = "123")
    private Integer provinceId;
    @NotNull(message = "P_ORDER_CITY_ID_NULL")
    @ApiModelProperty(value = "城市id(不存在可以填0)", required = false, example = "123")
    private Integer cityId;
    @NotNull(message = "P_ORDER_DISTRICT_ID_NULL")
    @ApiModelProperty(value = "区域id(不存在可以填0)", required = false, example = "123")
    private Integer districtId;
    @NotBlank(message = "P_ORDER_COUNTRY_NAME_NULL")
    @ApiModelProperty(value = "国家名称", required = true, example = "中国")
    private String countryName;
    @ApiModelProperty(value = "省份名称", required = false, example = "省份名称")
    private String provinceName;
    @ApiModelProperty(value = "城市名称", required = false, example = "城市名称")
    private String cityName;
    @ApiModelProperty(value = "区域名称", required = false, example = "区域名称")
    private String districtName;
    @ApiModelProperty(value = "详细地址", required = false, example = "详细地址")
    private String address;
    @ApiModelProperty(value = "邮编", required = false, example = "12344")
    private String zipCode;
    @NotBlank(message = "P_ORDER_USER_NAME_NULL")
    @ApiModelProperty(value = "收货人", required = true, example = "12344")
    private String userName;
    @NotBlank(message = "P_ORDER_MOBILE_NULL")
    @ApiModelProperty(value = "收货人手机号", required = true, example = "12344")
    private String mobile;
    @ApiModelProperty(value = "收货人固定电话", required = false, example = "12344")
    private String phone;
    @ApiModelProperty(value = "送货时间类型 1.工作日，2.任意时间 3.双休 4.指定日期", required = false, example = "12344")
    private Short deliveryType;
    @ApiModelProperty(value = "送货时间", required = false, example = "12344")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;
}