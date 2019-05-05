package com.bat.dubboapi.financial.subaccount.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderSubAccountCmd implements Serializable {


    private static final long serialVersionUID = -2475165132594069096L;


    private Integer distributorId;

    private String distributorName;

    /**
     * 微信订单支付凭证
     */
    private String outTradeNo;

    private Integer orderId;

    private String orderNo;

    private Integer shopId;

    private String shopName;

    private BigDecimal payAmount;

    private BigDecimal maxSubAccountAmount;

    private BigDecimal profitAccount;

    private BigDecimal actualSubAccountAmount;


    private List<SubAccountReceiveCmd> receiveCmdList;



}
