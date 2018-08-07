package com.bat.order.dao.data.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class OrderCustomerDataDO {
    private Integer id;

    private Integer orderId;

    private Integer customerId;

    private String customerName;

    private String shopCode;

    private String shopName;

    private Short orderStatus;

    private Short payStatus;

    private Date payTime;

    private Short payWay;

    private Short customerMode;

    private String remark;

    private String cancelRemark;

    private Date createTime;

    private Date updateTime;

    private Integer distributorId;

    private String distributorName;

    private String companyName;
}