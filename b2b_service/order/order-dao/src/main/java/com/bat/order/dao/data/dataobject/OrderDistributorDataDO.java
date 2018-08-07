package com.bat.order.dao.data.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderDistributorDataDO {
    private Integer id;

    private Integer orderId;

    private Integer distributorId;

    private String distributorName;

    private Integer distributorContactId;

    private String distributorContactName;

    private Short distributionMode;

    private Short erpFlag;

    private String companyName;

    private Integer distributorAncestorId;

    private String distributorAncestorName;

    private String distributorAncestorCompanyName;

    private Short directFlag;

    private Integer treeNode;

    private Short orderStatus;

    private Short payStatus;

    private Date payTime;

    private Short payWay;

    private String currencyType;

    private BigDecimal currentRates;

    private String remark;

    private String cancelRemark;

    private Date createTime;

    private Date updateTime;

}