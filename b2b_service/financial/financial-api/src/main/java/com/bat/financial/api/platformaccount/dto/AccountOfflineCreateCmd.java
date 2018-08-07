package com.bat.financial.api.platformaccount.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 13:46
 */
@Data
@ApiModel(value = "AccountOfflineCreateCmd", description = "平台收款账户(线下)新增")
public class AccountOfflineCreateCmd {

    @NotNull(message = "P_ACCOUNT_OFFLINE_ORGANIZATION_ID_NULL")
    @ApiModelProperty(value = "销售组织id", required = true, example = "1")
    private Integer organizationId;

    @NotBlank(message = "P_ACCOUNT_OFFLINE_CURRENCY_CODE_NULL")
    @ApiModelProperty(value = "币别编码", required = true, example = "CNY")
    private String currencyCode;

    @NotBlank(message = "P_ACCOUNT_OFFLINE_ACCOUNT_NAME_NULL")
    @ApiModelProperty(value = "收款账户名称", required = true, example = "")
    private String accountName;

    @ApiModelProperty(value = "erp账户编码", required = false, example = "00001")
    private String erpAccountNo;

    @NotBlank(message = "P_ACCOUNT_OFFLINE_BANK_NO_NULL")
    @ApiModelProperty(value = "erp银行账号", required = true, example = "1341583701@1341583701")
    private String bankNo;

    @NotBlank(message = "P_ACCOUNT_OFFLINE_CARD_NO_NULL")
    @ApiModelProperty(value = "银行卡号", required = true, example = "wx63c410d5de384240")
    private String cardNo;

    @NotBlank(message = "P_ACCOUNT_OFFLINE_BANK_NAME_NULL")
    @ApiModelProperty(value = "开户行名称", required = true, example = "wx63c410d5de384240")
    private String bankName;

    @NotBlank(message = "P_ACCOUNT_OFFLINE_BANK_ADDR_NULL")
    @ApiModelProperty(value = "开户行地址", required = true, example = "wx63c410d5de384240")
    private String bankAddr;

    @NotNull(message = "P_ACCOUNT_OFFLINE_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1启用,0停用", required = true, example = "1")
    private Short openFlag;

}
