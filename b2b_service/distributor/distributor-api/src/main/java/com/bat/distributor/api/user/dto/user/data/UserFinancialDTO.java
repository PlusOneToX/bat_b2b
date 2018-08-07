package com.bat.distributor.api.user.dto.user.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商财务信息")
public class UserFinancialDTO {

    private Integer id;
    @ApiModelProperty(value = "结算方式", example = "30天")
    private String trade;
    @ApiModelProperty(value = "结算方式(英文)", example = "30天")
    private String tradeEn;
    @ApiModelProperty(value = "货币类型 1-人民币 2-美元", example = "bat")
    private Short coinType;
    @ApiModelProperty(value = "银行账户名", example = "中国银行")
    private String bankAccountName;
    @ApiModelProperty(value = "开户行全称", example = "中国银行")
    private String bankDepositName;
    @ApiModelProperty(value = "银行账户", example = "123245675")
    private String bankAccount;
    @ApiModelProperty(value = "税种类型 1-一般纳税人 2-小规模纳税人 3-个人", example = "1")
    private Short taxType;
    @ApiModelProperty(value = "发票抬头名称", example = "发票抬头名称")
    private String invoiceTitleName;
    @ApiModelProperty(value = "纳税人识别号", example = "156575")
    private String taxpayerNumber;
    @ApiModelProperty(value = "发票注册地址", example = "发票注册地址")
    private String registeredAddress;
    @ApiModelProperty(value = "发票注册电话", example = "15674325")
    private String registeredPhone;
    @ApiModelProperty(value = "发票开户银行", example = "中国银行")
    private String registeredBankDepositName;
    @ApiModelProperty(value = "发票银行账户", example = "143764375")
    private String registeredBankAccount;

}
