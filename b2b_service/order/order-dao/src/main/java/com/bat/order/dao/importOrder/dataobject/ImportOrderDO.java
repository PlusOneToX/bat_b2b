package com.bat.order.dao.importOrder.dataobject;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ImportOrderDO {
    private Integer id;

    private String erpDistributorNo;

    private Integer distributorId;

    private String distributorName;

    private String orderTypeValue;

    private String orderTypeName;

    private Integer countryId;

    private Integer provinceId;

    private Integer cityId;

    private Integer districtId;

    private String countryName;

    private String provinceName;

    private String cityName;

    private String districtName;

    private String address;

    private String zipCode;

    private String userName;

    private String mobile;

    private String phone;

    private Short payWay;

    private Short deliveryType;

    private Date deliveryTime;

    private Integer distributionId;

    private String distributionName;

    private Short invoiceType;

    private String remark;

    private Short handleFlag;

    private String orderId;

    private String orderNo;

    private Date orderCreateTime;

    private Date createTime;

    private Date updateTime;

    private Integer countSum;

    private BigDecimal amountSum;

    private Short isInvoice;

    private Integer operateUid;

    private String currencyType;

    private BigDecimal currencyRates;

    private Short submitStatus;

    private String orderSplitFlag;

    private String manufactor;

    private String remind;
}