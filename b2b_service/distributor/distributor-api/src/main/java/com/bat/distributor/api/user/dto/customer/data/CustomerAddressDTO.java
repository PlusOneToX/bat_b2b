package com.bat.distributor.api.user.dto.customer.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "C端客户地址信息")
public class CustomerAddressDTO {

    private Integer id;
    @ApiModelProperty(value = "收货人姓名", example = "bat")
    private String userName;
    @ApiModelProperty(value = "收货人电话", example = "bat20186")
    private String phone;
    @ApiModelProperty(value = "省份id", example = "1234")
    private Integer provinceId;
    @ApiModelProperty(value = "省份名称", example = "1234")
    private String provinceName;
    @ApiModelProperty(value = "城市id", example = "1234")
    private Integer cityId;
    @ApiModelProperty(value = "城市名称", example = "1234")
    private String cityName;
    @ApiModelProperty(value = "区id", example = "1234")
    private Integer districtId;
    @ApiModelProperty(value = "区名称", example = "1234")
    private String districtName;
    @ApiModelProperty(value = "地址", example = "地址")
    private String address;
    @ApiModelProperty(value = "邮编", example = "123455")
    private String zipCode;
    @ApiModelProperty(value = "是否默认收货地址 0.否 1.是", example = "0")
    private Short defaultFlag;
}
