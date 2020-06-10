package com.bat.flexible.dao.exchange.co;

import lombok.Data;

import java.util.Date;

@Data
public class ExchangeCodeOrderPageCO {
    

    private Integer codeId;

    /**
     * 分销商
     */
    private String distributorName;

    /**
     * 分销商订单id
     */
    private Integer distributorOrderId;

    /**
     * 收货人
     */
    private String receiveName;

    /**
     * 收货电话
     */
    private String receiveMobile;


    /**
     * 活动名称
     */
    private String activityName;


    /**
     * 明码
     */
    private String plainCode;


    /**
     * 盒码
     */
    private String boxCode;

    /**
     * 批次id
     */
    private Integer exchangeFactoryId;


    /**
     * 核销订单id
     */
    private Integer userOrderId;

    /**
     * 订单状态 1.待确认2.已确认 3. 部分发货 4. 全部发货 5.已关闭  6.已完成
     */
    private Byte orderStatus;

    /**
     * 订单支付方式 1.支付宝,2.微信,3.区间结算,4.公司转账,5.余额支付,6.快钱支付
     */
    private Short payWay;

    /**
     * 核销时间
     */
    private Date useTime;


    /**
     * 核销状态 0、未激活、1、未使用 2、已核销 3、已过期 4、已作废
     */
    private Short status;




    
}