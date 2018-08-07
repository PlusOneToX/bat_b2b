package com.bat.distributor.api.user.dto.customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "C端客户收货地址信息")
public class CustomerAddressCmd {

    @ApiModelProperty(value = "地址id（修改时必填）", required = false, example = "bat")
    private Integer id;
    @NotNull(message = "P_DISTRIBUTOR_CUSTOMER_ID_NULL")
    @ApiModelProperty(value = "C端客户id", required = true, example = "bat")
    private Integer customerId;
    @NotBlank(message = "P_DISTRIBUTOR_USER_ADDRESS_NAME_NULL")
    @ApiModelProperty(value = "收货人姓名", required = true, example = "bat")
    private String userName;
    @NotBlank(message = "P_DISTRIBUTOR_USER_ADDRESS_PHONE_NULL")
    @ApiModelProperty(value = "收货人电话", required = true, example = "bat20186")
    private String phone;
    @NotNull(message = "P_DISTRIBUTOR_PROVINCE_ID_NULL")
    @ApiModelProperty(value = "省份id", required = true, example = "1234")
    private Integer provinceId;
    @NotNull(message = "P_DISTRIBUTOR_PROVINCE_NAME_NULL")
    @ApiModelProperty(value = "省份名称", required = true, example = "1234")
    private String provinceName;
    @NotNull(message = "P_DISTRIBUTOR_CITY_ID_NULL")
    @ApiModelProperty(value = "城市id", required = true, example = "1234")
    private Integer cityId;
    @NotNull(message = "P_DISTRIBUTOR_CITY_NAME_NULL")
    @ApiModelProperty(value = "城市名称", required = true, example = "1234")
    private String cityName;
    @ApiModelProperty(value = "区id", required = false, example = "1234")
    private Integer districtId;
    @ApiModelProperty(value = "区名称", required = false, example = "1234")
    private String districtName;
    @NotBlank(message = "P_DISTRIBUTOR_USER_ADDRESS_NULL")
    @ApiModelProperty(value = "详细地址", required = true, example = "地址")
    private String address;
    @ApiModelProperty(value = "邮编", required = false, example = "123455")
    private String zipCode;
    @ApiModelProperty(value = "是否默认收货地址 0.否 1.是", required = true, example = "0")
    private Short defaultFlag;
}
