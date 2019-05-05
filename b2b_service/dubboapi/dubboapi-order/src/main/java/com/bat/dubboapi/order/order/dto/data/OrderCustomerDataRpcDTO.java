package com.bat.dubboapi.order.order.dto.data;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderCustomerDataRpcDTO implements Serializable {
    private static final long serialVersionUID = 5840506799282936087L;
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