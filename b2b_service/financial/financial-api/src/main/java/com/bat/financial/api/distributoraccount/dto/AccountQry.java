package com.bat.financial.api.distributoraccount.dto;

import javax.validation.constraints.NotNull;

import com.bat.financial.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 10:09
 */
@Data
@ApiModel(value = "DistributorAccountQry", description = "平台账户查询")
public class AccountQry extends BaseSearchQry {

    public AccountQry() {
        super.attributeMapper.put((short)1, "setAccountName");
        super.attributeMapper.put((short)2, "setAccountNo");
        super.attributeMapper.put((short)3, "setAppId");
        super.attributeMapper.put((short)4, "setDistributorName");
        super.attributeMapper.put((short)5, "setDistributorCompanyName");
    }

    @ApiModelProperty(value = "应用类型 1、微信公众号 2、微信小程序", example = "1")
    private Short appType;

    @ApiModelProperty(value = "1.微信公众号名称", example = "1")
    private String accountName;

    @ApiModelProperty(value = "2.商户号", example = "1")
    private String accountNo;

    @ApiModelProperty(value = "3.appId", example = "1")
    private String appId;

    @ApiModelProperty(value = "4.分销商名称", example = "1")
    private String distributorName;

    @ApiModelProperty(value = "5.分销商公司名称", example = "1")
    private String distributorCompanyName;

    @NotNull(message = "P_DEPOSIT_DISTRIBUTOR_USER_NULL")
    @ApiModelProperty(value = "当前用户", required = true, example = "1")
    private Integer userId;

}
