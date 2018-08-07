package com.bat.distributor.api.subaccount.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SubAccountSalemanPageDTO {

    @ApiModelProperty(value = "分销商名称")
    private String distributorName;

    @ApiModelProperty(value = "业务员ID")
    private Integer id;

    @ApiModelProperty(value = "业务员姓名")
    private String salemanName;

    @ApiModelProperty(value = "身份类型 1、企业 2、个人")
    private Short type;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "等级ID")
    private Integer levelId;

    @ApiModelProperty(value = "级别")
    private String levelName;

    @ApiModelProperty(value = "上级业务员ID")
    private Integer parentId;

    @ApiModelProperty(value = "上级分销商业务员名称")
    private String parentSalemanName;

    @ApiModelProperty(value = "商户号")
    private String merchantNumber;

    @ApiModelProperty(value = "openId")
    private String openId;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;



}
