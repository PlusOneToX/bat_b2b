package com.bat.financial.api.deposit.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 18:37
 */
@Data
public class DepositDistributorExtendDTO {
    private Integer id;

    private Integer distributorId;

    private Integer treeNode;

    private Integer distributorAncestorId;

    private BigDecimal accountBalance;

    private BigDecimal accountAvailable;

    private BigDecimal freezingAmount;

    private BigDecimal rechargeAmount;

    private BigDecimal commissionAmount;

    private BigDecimal withdrawAmount;

    private BigDecimal consumerAmount;

    private BigDecimal refundAmount;

    private Date createTime;

    private Date updateTime;
}
