package com.bat.order.dao.order.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 后台管理订单列表自定义类
 */
@Data
public class AdminOrderInfoListDO {
    private Integer id;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * erp订单号
     */
    private String orderErpNo;
    /**
     * 第三方单号
     */
    private String orderThirdpartyNo;
    /**
     * 下单时间
     */
    private Date createTime;
    /**
     * 多级分销级数
     */
    private Integer treeNode;
    /**
     * 直属分销商id
     */
    private Integer distributorId;
    /**
     * 直属分销商名
     */
    private String distributorName;
    /**
     * 下单分销商id
     */
    private Integer orderDistributorId;
    /**
     * 下单分销商名
     */
    private String orderDistributorName;
    /**
     * 同步erp分销商id
     */
    private Integer syncErpDistributorId;
    /**
     * 同步erp分销商名
     */
    private String syncErpDistributorName;
    /**
     * 来源平台
     */
    private String userSource;
    /**
     * 来源平台名称
     */
    private String userSourceName;
    /**
     * 店铺编码
     */
    private String shopCode;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 收货人
     */
    private String userName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 支付币种
     */
    private String currencyType;
    /**
     * 下单汇率
     */
    private BigDecimal currentRates;
    /**
     * 订单应付金额
     */
    private BigDecimal payAmount;
    /**
     * 备注
     */
    private String remark;
    /**
     * bat结算金额
     */
    private BigDecimal settlementAmount;
    /**
     * 付款状态
     */
    private Short payStatus;
    /**
     * 支付方式
     */
    private Short payWay;
    /**
     * 收款人
     */
    private String payee;
    /**
     * 订单状态
     */
    private Short orderStatus;
    /**
     * 物流状态
     */
    private Short deliverStatus;

    /**
     * 支付凭证号(注意：全平台唯一)
     */
    private String outTradeNo;
}