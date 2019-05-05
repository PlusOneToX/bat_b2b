package com.bat.dubboapi.order.order.dto.info;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderInfoRpcQryDTO implements Serializable {
    private static final long serialVersionUID = -6676937130377981915L;
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