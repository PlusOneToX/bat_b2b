package com.bat.financial.api.deposit.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 18:37
 */
@Data
public class DepositDistributorDTO {
    @ExcelProperty(value = "#P_CURRENCY_PAGE_SIZE_NULL")
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称")
    private String distributorName;

    @ApiModelProperty(value = "分销商内码")
    private String erpDistributorId;

    @ApiModelProperty(value = "分销商级数")
    private Integer treeNode;

    @ApiModelProperty(value = "父分销商id")
    private Integer distributorAncestorId;

    @ApiModelProperty(value = "父分销商名称")
    private String distributorAncestorName;

    @ApiModelProperty(value = "账户余额")
    private BigDecimal accountBalance;

    @ApiModelProperty(value = "账户可用余额")
    private BigDecimal accountAvailable;

    @ApiModelProperty(value = "冻结总金额")
    private BigDecimal freezingAmount;

    @ApiModelProperty(value = "充值总金额")
    private BigDecimal rechargeAmount;

    @ApiModelProperty(value = "佣金总金额")
    private BigDecimal commissionAmount;

    @ApiModelProperty(value = "提现总金额")
    private BigDecimal withdrawAmount;

    @ApiModelProperty(value = "消费总金额")
    private BigDecimal consumerAmount;

    @ApiModelProperty(value = "退款总金额")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
