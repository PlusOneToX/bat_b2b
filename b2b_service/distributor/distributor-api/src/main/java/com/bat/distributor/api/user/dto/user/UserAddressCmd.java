package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商地址(包括收货地址)信息")
public class UserAddressCmd {

    @ApiModelProperty(value = "地址id（修改时必填）", required = false, example = "bat")
    private Integer id;
    @NotNull(message = "P_DISTRIBUTOR_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "分销商（客户）id", required = true, example = "bat")
    private Integer distributorId;
    @NotBlank(message = "P_DISTRIBUTOR_USER_ADDRESS_NAME_NULL")
    @ApiModelProperty(value = "收货人姓名", required = true, example = "bat")
    private String userName;
    @NotBlank(message = "P_DISTRIBUTOR_USER_ADDRESS_PHONE_NULL")
    @ApiModelProperty(value = "收货人电话", required = true, example = "bat20186")
    private String phone;
    @NotNull(message = "P_DISTRIBUTOR_COUNTRY_ID_NULL")
    @ApiModelProperty(value = "国家id", required = true, example = "1234")
    private Integer countryId;
    @ApiModelProperty(value = "省份id(国家为中国时必填)", required = false, example = "1234")
    private Integer provinceId;
    @ApiModelProperty(value = "城市id(国家为中国时必填)", required = false, example = "1234")
    private Integer cityId;
    @ApiModelProperty(value = "区id", required = false, example = "1234")
    private Integer districtId;
    @NotBlank(message = "P_DISTRIBUTOR_USER_ADDRESS_NULL")
    @ApiModelProperty(value = "详细地址", required = true, example = "地址")
    private String address;
    @ApiModelProperty(value = "邮编", required = false, example = "123455")
    private String zipCode;
    @NotNull(message = "P_DISTRIBUTOR_ADDRESS_TYPE_NULL")
    @ApiModelProperty(value = "地址类型 1.公司地址 2.收货地址（固定传2）", required = true, example = "2")
    private Short addressType;
    @ApiModelProperty(value = "是否默认收货地址 0.否 1.是", required = true, example = "0")
    private Short defaultFlag;
}
