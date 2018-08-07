package com.bat.distributor.api.subaccount.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubAccountUserConfigPageDTO {

    @ApiModelProperty(value = "分账配置主键id")
    private Integer id;

    @ApiModelProperty(value = "分销商名称")
    private String distributorName;

    @ApiModelProperty(value = "分账配置名称")
    private String name;

    @ApiModelProperty(value = "分账金额类型 1、按照实付金额 2、按照利润金额")
    private Short amountType;

    @ApiModelProperty(value = "等级比例列表")
    private List<SubAccountLevelRatioDTO> levelRatioList;

}
