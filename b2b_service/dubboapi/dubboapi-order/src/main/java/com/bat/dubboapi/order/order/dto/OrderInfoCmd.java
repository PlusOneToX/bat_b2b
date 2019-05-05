package com.bat.dubboapi.order.order.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/19 14:00
 */
public class OrderInfoCmd implements Serializable {
    private static final long serialVersionUID = -5325998212430866558L;
    /**
     * 分销商id
     */
    private Integer distributorId;

    /**
     * 是否开具发票 0.否，1.是
     */
    private Short invoiceFlag;
    /**
     * 订单来源：平台编码 platform
     */
    private String orderSource;
    /**
     * 结算币种 默认传人民币CNY
     */
    private String currencyType;
    /**
     * 付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付
     */
    private Short payWay;
    /**
     * 下单商品明细列表
     */
    private List<OrderGoodsCmd> goodss;
    /**
     * 配送方式列表
     */
    private List<OrderLogisticsCmd> logisticss;
    /**
     * 收货地址信息
     */
    private OrderDeliveryCmd delivery;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * 第三方客户订单编号
     */
    private String orderThirdpartyNo;

    public Short getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(Short invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Short getPayWay() {
        return payWay;
    }

    public void setPayWay(Short payWay) {
        this.payWay = payWay;
    }

    public List<OrderGoodsCmd> getGoodss() {
        return goodss;
    }

    public void setGoodss(List<OrderGoodsCmd> goodss) {
        this.goodss = goodss;
    }

    public List<OrderLogisticsCmd> getLogisticss() {
        return logisticss;
    }

    public void setLogisticss(List<OrderLogisticsCmd> logisticss) {
        this.logisticss = logisticss;
    }

    public OrderDeliveryCmd getDelivery() {
        return delivery;
    }

    public void setDelivery(OrderDeliveryCmd delivery) {
        this.delivery = delivery;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderThirdpartyNo() {
        return orderThirdpartyNo;
    }

    public void setOrderThirdpartyNo(String orderThirdpartyNo) {
        this.orderThirdpartyNo = orderThirdpartyNo;
    }
}
