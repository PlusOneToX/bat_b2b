package com.bat.dubboapi.order.order.dto.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderExtendDataSimpleRpcDTO implements Serializable {
    private static final long serialVersionUID = -8231864103010029474L;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 订单号（B2B订单号、非ERP订单号）
     */
    private String orderNo;

    /**
     * ERP订单号（相当于之前的订单号）
     */
    private String orderErpNo;

    /**
     * 第三方客户订单编号
     */
    private String orderThirdpartyNo;

    /**
     * 工厂单号
     */
    private String orderFactoryNo;

    /**
     * 工厂编码
     */
    private String factoryCode;
    /**
     * 是否自动下推出库 1、是 0、否
     */
    private Short autoDelivery;

    /**
     * 下单时openId
     */
    private String openId;


}

