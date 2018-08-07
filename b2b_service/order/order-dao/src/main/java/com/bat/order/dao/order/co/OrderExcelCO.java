package com.bat.order.dao.order.co;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class OrderExcelCO {

    private Integer orderId;

    private Date createTime;

    private String orderNo;

    private String orderTypeId;

    private String orderStatus;

    private String deliverStatus;

    private String currencyType;

    private BigDecimal currentRates;

    private String payStatus;

    private String payWay;

    private String remark;

    private String orderErpNo;

    private String erpOutboundNo;

    private String orderDeliverBillId;

    private Integer distributorId;

    private String distributorName;

    private String distributorErpId;

    private Integer salesId;

    private String salesName;

    private String departmentName;

    private String receiver;

    private String receiveAddress;

    private String contactMobile;

    private String distributionName;

    private String logisticsNo;

    private String itemCode;

    private String itemName;

    private Integer itemCount;

    private Integer deliverCount;

    private String orderFactoryNo;

    private String orderThirdpartyNo;

    private String materialName;

    private String modelName;

    private Integer pictureId;

    private BigDecimal salePrice;

    // 零售价
    private BigDecimal retailPrice;

    private BigDecimal actualPrice;

    private BigDecimal salePriceSum;

    private BigDecimal actualPriceSum;

    // C端下单总额
    private BigDecimal userActualPrice;

    private Integer promotionId;

    private String shopCode;

    private String shopName;

    // 订单货品种类数量
    private Integer orderLength = 1;

    private Integer itemId;
}
