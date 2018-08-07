package com.bat.order.dao.data.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class OrderExtendDataDO {
    private Integer id;

    private Integer orderId;

    private String orderThirdpartyNo;

    private String orderErpNo;

    private String orderErpType;

    private String orderFactoryNo;

    private String factoryCode;

    private Date createTime;

    private Date updateTime;

    private Short autoDelivery;

    private String openId;
}