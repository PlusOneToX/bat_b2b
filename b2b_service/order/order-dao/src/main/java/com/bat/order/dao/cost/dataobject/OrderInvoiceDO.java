package com.bat.order.dao.cost.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class OrderInvoiceDO {
    private Integer id;

    private Integer orderId;

    private Short invoiceType;

    private Short invoiceTitleType;

    private String taxpayerNumber;

    private String name;

    private String registerAddress;

    private String registerPhone;

    private String bankAccountName;

    private String bankAccount;

    private Date createTime;

    private Date updateTime;
}