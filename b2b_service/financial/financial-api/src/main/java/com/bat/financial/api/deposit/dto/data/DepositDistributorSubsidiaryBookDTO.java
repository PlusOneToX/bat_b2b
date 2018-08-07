package com.bat.financial.api.deposit.dto.data;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/7 11:16
 */
@Data
public class DepositDistributorSubsidiaryBookDTO {
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 账户余额id
     */
    @ApiModelProperty(value = "账户余额id")
    private Integer depositDistributorId;

    /**
     * 分销商id
     */
    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    /**
     * 分销商名称
     */
    @ApiModelProperty(value = "分销商名称")
    private String distributorName;

    /**
     * 业务类型 1充值 2 提现 3 订单消费 4 订单取消增加 5调整 6 ERP增量变化 7 ERP全量变化 8 分销佣金
     */
    @ApiModelProperty(value = "业务类型 1充值 2 提现 3 订单消费 4 订单取消增加 5调整 6 ERP增量变化 7 ERP全量变化 8 分销佣金")
    private Short businessType;

    /**
     * 交易方式付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付
     */
    @ApiModelProperty(value = "交易方式付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付")
    private Short payWay;

    /**
     * 业务单号 (业务类型为 6或7时，为空 )
     */
    @ApiModelProperty(value = "分销商名称")
    private String businessId;

    /**
     * 变更类型 1.增加，2.减少
     */
    @ApiModelProperty(value = "变更类型 1.增加，2.减少")
    private Short changeType;

    /**
     * 变动金额数
     */
    @ApiModelProperty(value = "变动金额数")
    private BigDecimal amount;

    /**
     * 变化前账户余额
     */
    @ApiModelProperty(value = "变化前账户余额")
    private BigDecimal beforeDepositAmount;

    /**
     * 变化后账户余额
     */
    @ApiModelProperty(value = "变化后账户余额")
    private BigDecimal afterDepositAmount;

    /**
     * 备注说明
     */
    @ApiModelProperty(value = "备注说明")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
