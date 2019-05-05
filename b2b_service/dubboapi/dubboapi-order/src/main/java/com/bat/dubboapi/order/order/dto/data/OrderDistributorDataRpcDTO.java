package com.bat.dubboapi.order.order.dto.data;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderDistributorDataRpcDTO implements Serializable {

    private static final long serialVersionUID = -3011560345179474047L;

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

    private Short payWay;

    private String currencyType;

    private BigDecimal currentRates;

    private String remark;

}
