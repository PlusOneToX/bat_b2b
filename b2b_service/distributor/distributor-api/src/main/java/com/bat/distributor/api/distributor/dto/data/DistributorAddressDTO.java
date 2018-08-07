package com.bat.distributor.api.distributor.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商地址(包括收货地址)信息")
public class DistributorAddressDTO {

    private Integer id;
    @ApiModelProperty(value = "收货人姓名", required = false, example = "bat")
    private String userName;
    @ApiModelProperty(value = "收货人电话", required = false, example = "1234")
    private String phone;
    @ApiModelProperty(value = "国家id", required = true, example = "1234")
    private Integer countryId;
    @ApiModelProperty(value = "省份id", required = false, example = "1234")
    private Integer provinceId;
    @ApiModelProperty(value = "城市id", required = false, example = "1234")
    private Integer cityId;
    @ApiModelProperty(value = "区id", required = false, example = "1234")
    private Integer districtId;
    @ApiModelProperty(value = "地址", required = true, example = "地址")
    private String address;
    @ApiModelProperty(value = "邮编", required = false, example = "123455")
    private String zipCode;
    @ApiModelProperty(value = "地址类型 1.公司地址 2.收货地址", required = true, example = "1")
    private Short addressType;
    @ApiModelProperty(value = "是否默认收货地址 0.否 1.是", required = false, example = "0")
    private Short defaultFlag;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
