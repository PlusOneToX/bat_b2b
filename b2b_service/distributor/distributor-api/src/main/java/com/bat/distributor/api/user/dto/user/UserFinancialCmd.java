package com.bat.distributor.api.user.dto.user;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商财务信息")
public class UserFinancialCmd {

    private Integer id;
    @ApiModelProperty(value = "结算方式id", required = false, example = "1")
    private Integer tradeId;
    @NotNull(message = "P_DISTRIBUTOR_COIN_TYPE_NULL")
    @ApiModelProperty(value = "货币类型 1-人民币 2-美元", required = true, example = "bat")
    private Short coinType;
    @ApiModelProperty(value = "银行账户名", required = false, example = "中国银行")
    private String bankAccountName;
    @ApiModelProperty(value = "开户行全称", required = false, example = "中国银行")
    private String bankDepositName;
    @ApiModelProperty(value = "银行账户", required = false, example = "123245675")
    private String bankAccount;
    @ApiModelProperty(value = "税种类型 1-一般纳税人 2-小规模纳税人 3-个人", required = false, example = "1")
    private Short taxType;
    @ApiModelProperty(value = "发票抬头名称", required = false, example = "发票抬头名称")
    private String invoiceTitleName;
    @ApiModelProperty(value = "纳税人识别号", required = false, example = "156575")
    private String taxpayerNumber;
    @ApiModelProperty(value = "发票注册地址", required = false, example = "发票注册地址")
    private String registeredAddress;
    @ApiModelProperty(value = "发票注册电话", required = false, example = "15674325")
    private String registeredPhone;
    @ApiModelProperty(value = "发票开户银行", required = false, example = "中国银行")
    private String registeredBankDepositName;
    @ApiModelProperty(value = "发票银行账户", required = false, example = "143764375")
    private String registeredBankAccount;
}
