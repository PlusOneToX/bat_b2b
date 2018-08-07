package com.bat.order.api.importOrder.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/12 14:22
 */
@Data
public class ImportOrderDetailDTO {
    private Short onWayAttendEventFlag;
    private Short customizedAttendEventFlag;
    private Short mtoAttendEventFlag;

    private long importOrderId;

    /**
     * 分销商id
     */
    private Integer distributorId;
    /**
     * 分销商名称
     */
    private String distributorName;
    /**
     * 订单类型
     */
    private String orderTypeValue;
    /**
     * 订单类型名称
     */
    private String orderTypeName;
    /**
     * 国家
     */
    private Integer countryId;
    /**
     * 省份id
     */
    private Integer provinceId;
    /**
     * 市id
     */
    private Integer cityId;
    /**
     * 区/县id
     */
    private Integer districtId;

    /**
     * 地址
     */
    private String address;
    /**
     * 邮编
     */
    private String zipCode;
    /**
     * 收货人
     */
    private String userName;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 电话
     */
    private String phone;
    /**
     * 支付方式
     */
    private Short payWay;
    /**
     * 送货时间类型 1.工作日，2.任意时间 3.双休 4.指定日期
     */
    private Short deliveryType;
    /**
     * 送货时间
     */
    private Date deliveryTime;
    /**
     * 配送方式id
     */
    private Integer distributionId;

    /**
     * 是否开具发票 0.否，1.是
     */
    private Short isInvoice;
    /**
     * 发票类型 1.普通 2.增值税发票
     */
    private Short invoiceType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 下单状态 1:已下单 0:未下单
     */
    private Short handleFlag;

    private List<ImportOrderGoodsDTO> orderGoods;

    /**
     * 商品总额
     */
    private BigDecimal goodsAmount;
    /**
     * 服务费总额 重构后好像没了
     */
    private BigDecimal goodsServiceAmount = BigDecimal.ZERO;
    /**
     * 订单折扣
     */
    private BigDecimal orderDiscount;
    /**
     * 商品折扣
     */
    private BigDecimal goodsDiscount;
    /**
     * 发票税额
     */
    private BigDecimal invoiceTax;
    /**
     * 配送费用
     */
    private BigDecimal distributionMoney;

    /**
     * 订单总额
     */
    private BigDecimal orderAmount;

    /**
     * 在库商品总额
     */
    private BigDecimal amountSumInWarehouse;
    /**
     * 在途商品总额
     */
    private BigDecimal amountSumOnWay;

    /**
     * 订单id
     */
    private String orderId;
    /**
     * erp订单编号
     */
    private String orderNo;
    /**
     * 订单下单时间
     */
    private Date orderCreateTime;

    /**
     * 订单币种
     */
    private String currencyType;
    /**
     * 订单汇率
     */
    private BigDecimal currencyRates;

    private String remind;// 操作日志提示

    /**
     * 订单是否拆分 1、拆分 0、否、默认是拆
     */
    private String orderSplitFlag;

    private String manufactor; // 生产商 YC.云创 LHW.联辉王

}
