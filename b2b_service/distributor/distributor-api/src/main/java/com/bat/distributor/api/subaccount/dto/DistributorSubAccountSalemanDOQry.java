package com.bat.distributor.api.subaccount.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DistributorSubAccountSalemanDOQry {

    @ApiModelProperty(value = "业务员主键id")
    private Integer id;

    @ApiModelProperty(value = "身份类型 1、企业、2 个人")
    private Short type;

    @ApiModelProperty(value = "业务员姓名")
    private String name;

    @ApiModelProperty(value = "业务员手机号")
    private String mobile;

    @ApiModelProperty(value = "等级id")
    private Integer levelId;

    @ApiModelProperty(value = "父业务员id")
    private Integer parentId;

    @ApiModelProperty(value = "客户表id")
    private Integer customerId;

    @ApiModelProperty(value = "商户号")
    private String merchantNumber;

    @ApiModelProperty(value = "状态 1、启用 0、禁用")
    private Short openFlag;

    @ApiModelProperty(value = "归属分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "排序号")
    private Integer sequence;

}