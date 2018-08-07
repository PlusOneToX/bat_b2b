package com.bat.order.api.cost.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: 默认发票信息 目前订单只有一条开票数据
 * @date: 2018/6/28 12:10
 */
@Data
public class OrderInvoiceDTO {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单ID")
    private Integer orderId;
    @ApiModelProperty("发票类型 1.普通 2.增值税发票")
    private Short invoiceType;
    @ApiModelProperty("发票抬头 1.个人 2.单位")
    private Short invoiceTitleType;
    @ApiModelProperty("纳税人识别号(发票抬头为2时必填)")
    private String taxpayerNumber;
    @ApiModelProperty("发票名称 (个人姓名或单位名称)")
    private String name;
    @ApiModelProperty("公司注册地址")
    private String registerAddress;
    @ApiModelProperty("公司注册电话")
    private String registerPhone;
    @ApiModelProperty("银行账户名")
    private String bankAccountName;
    @ApiModelProperty("银行账号")
    private String bankAccount;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;

}
