package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

@Data
public class DistributorRpcDO {
    private Integer id;
    private String name;
    private String companyName;
    /**
     * 业务员
     */
    private Integer salesId;
    /**
     * 商务员
     */
    private Integer coordinatorId;
    private Short freezeStatus;
    private Short applyStatus;
    private Short profileStatus;
    /**
     * 分销商级数
     */
    private Integer treeNode;
    private Integer erpId;
    private String erpNo;
    /**
     * 是否直发
     */
    private Short autoDelivery;
    private Integer tradeId;
    private Short payWay;
    private String erpSettleAccountNo;
    /**
     * 是否支持在途库存 1是 0否
     */
    private Short onWayFlag;
    /**
     * 信息是否同步到erp 1.是 0.否
     */
    private Short erpFlag;
    /**
     * ERP余额是否同步 1.是 0.否
     */
    private Short erpBalanceFlag;

    /**
     * 分销商付款方式： 1支付宝, 2微信，3银行（当分销商结算方式为3的时候也就是自己收款的时候，这个分销商付款方式就生效了，分校层数据中上层分销商的付款方式就跟这个字段走）
     */
    private Short distributionPayWay;

    /**
     * 订单类型id
     */
    private Integer orderTypeId;
    private Short customerFlag;
    private Short customerMode;
}