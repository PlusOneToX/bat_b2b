package com.bat.order.dao.order.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: lim
 * @description: 前台分销订单列表自定义类
 * @date: 2018/7/9 20:55
 */
@Data
public class UserDistributorOrderInfoListDO {
    /**
     * id
     */
    private Integer id;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 下单时间
     */
    private Date createTime;
    /**
     * 收货人
     */
    private String userName;
    /**
     * 付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付
     */
    private Short payWay;
    /**
     * 付款状态
     */
    private Short payStatus;
    /**
     * 订单状态
     */
    private Short orderStatus;
    /**
     * 物流状态
     */
    private Short deliverStatus;

    /**
     * 订单币种
     */
    private String currencyType;
    /**
     * 订单总额
     */
    private BigDecimal payAmount;
}
