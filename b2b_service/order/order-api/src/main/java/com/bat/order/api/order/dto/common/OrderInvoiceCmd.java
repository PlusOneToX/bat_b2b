package com.bat.order.api.order.dto.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderInvoiceCmd {
    // @NotNull(message = "P_ORDER_INVOICE_TYPE_NULL")
    @ApiModelProperty(value = "发票类型 1.普通 2.增值税发票", required = true, example = "1")
    private Short invoiceType;
    // @NotNull(message = "P_ORDER_INVOICE_TITLE_TYPE_NULL")
    @ApiModelProperty(value = "发票抬头类型 1.个人 2.单位", required = true, example = "1")
    private Short invoiceTitleType;
    @ApiModelProperty(value = "纳税人识别号(发票抬头为2时必填)", required = false, example = "574t4343232")
    private String taxpayerNumber;
    // @NotBlank(message = "P_ORDER_INVOICE_NAME_NULL")
    @ApiModelProperty(value = "发票名称 (个人姓名或单位名称)", required = true, example = "发票名称")
    private String name;
    @ApiModelProperty(value = "公司注册地址(发票抬头为2时必填)", required = false, example = "公司注册地址")
    private String registerAddress;
    @ApiModelProperty(value = "公司注册电话(发票抬头为2时必填)", required = false, example = "公司注册电话")
    private String registerPhone;
    @ApiModelProperty(value = "银行账户名(发票抬头为2时必填)", required = false, example = "银行账户名")
    private String bankAccountName;
    @ApiModelProperty(value = "银行账号(发票抬头为2时必填)", required = false, example = "银行账号")
    private String bankAccount;
}
