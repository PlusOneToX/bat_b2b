package com.bat.order.dao.order.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class OrderInfoDO {
    private Integer id;

    private String orderNo;

    private Short deliverStatus;

    private Date deliverTime;

    private Integer orderTypeId;

    private Short stockType;

    private Integer salesId;

    private String salesName;

    private Integer coordinatorId;

    private String coordinatorName;

    private Integer organizationId;

    private String organizationErp;

    private String organizationName;

    private Short invoiceFlag;

    private String orderSource;

    private String language;

    private Date createTime;

    private Date updateTime;

}