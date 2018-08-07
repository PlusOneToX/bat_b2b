package com.bat.order.service.common.data.dto;

import lombok.Data;

@Data
public class OrderInvoiceDTO {
    /**
     * 发票类型 1.普通 2.增值税发票
     */
    private Short invoiceType;
    /**
     * 发票抬头类型 1.个人 2.单位
     */
    private Short invoiceTitleType;
    /**
     * 纳税人识别号(发票抬头为2时必填)
     */
    private String taxpayerNumber;
    /**
     * 发票名称 (个人姓名或单位名称)
     */
    private String name;
    /**
     * 公司注册地址(发票抬头为2时必填)
     */
    private String registerAddress;
    /**
     * 公司注册电话(发票抬头为2时必填)
     */
    private String registerPhone;
    /**
     * 银行账户名(发票抬头为2时必填)
     */
    private String bankAccountName;
    /**
     * 银行账号(发票抬头为2时必填)
     */
    private String bankAccount;
}