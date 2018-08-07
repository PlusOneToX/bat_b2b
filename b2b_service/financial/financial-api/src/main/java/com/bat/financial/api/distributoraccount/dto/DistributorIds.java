package com.bat.financial.api.distributoraccount.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 10:09
 */
@Data
@ApiModel(value = "AccountQry", description = "平台账户id")
public class DistributorIds {

    // @NotNull(message = "P_ACCOUNT_ID_NULL")
    @ApiModelProperty(value = "ids")
    private List<Integer> ids;

    @ApiModelProperty(value = "应用类型 1、微信公众号 2、微信小程序", required = false, example = "1")
    private Short appType;

}
