package com.bat.dubboapi.order.order.dto.data;

import java.io.Serializable;
import java.util.List;

public class OrderTradeRpcDTO implements Serializable {
    /**
     * 1 平台方收款(比如：bat收款，bat收款), 2 非平台方（按分销商收款）
     */
    private Short tradeMode;
    /**
     * 组织id
     */
    private Integer organizationId;
    /**
     * 收款方分销商id
     */
    private Integer distributorId;
    /**
     * 交易方类型： 1分销商 2 C端客户
     */
    private Short counterpartyType;
    /**
     * 付款方式 1.支付宝,2.微信,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付
     */
    private Short payWay;
    /**
     * 币种 如“CNY”
     */
    private String currencyType;
    /**
     * 订单费用
     */
    List<OrderCostRpcDTO> orderCosts;

    /**
     * B_TRADE_SUCCESS=获取订单支付信息成功
     * B_TRADE_PAY_WAY_ERROR=合并支付订单支付类型必须一致
     * B_TRADE_PAID_ERROR=已支付订单不能重复支付
     * B_TRADE_CURRENCY_TYPE_ERROR=合并支付订单支付币种必须一致
     */
    private String flag;
    /**
     * 错误信息提示
     */
    private String msg;

    /**
     * 是否开启C端模式 0 不开启, 1 开启
     */
    private Short customerFlag;

    /**
     * 该值属于分销商配置、与财务的tradeMode不一致、请勿混在一起
     * C端结算方式： 1 平台方收款(比如：bat收款，bat收款), 2 上级收款 3 自己收款(分销商自己收款)
     */
    private Short customerMode;

    /**
     * 是否开启分账 1、是 0、否（开启C端模式并且C端结算模式属于自己收款才有值）
     */
    private Short subAccountFlag;

    /**
     * 是否门店订单
     */
    private Boolean shopOrderFlag = false;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Short getTradeMode() {
        return tradeMode;
    }

    public void setTradeMode(Short tradeMode) {
        this.tradeMode = tradeMode;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Short getCounterpartyType() {
        return counterpartyType;
    }

    public void setCounterpartyType(Short counterpartyType) {
        this.counterpartyType = counterpartyType;
    }

    public Short getPayWay() {
        return payWay;
    }

    public void setPayWay(Short payWay) {
        this.payWay = payWay;
    }

    public List<OrderCostRpcDTO> getOrderCosts() {
        return orderCosts;
    }

    public void setOrderCosts(List<OrderCostRpcDTO> orderCosts) {
        this.orderCosts = orderCosts;
    }

    public Short getCustomerFlag() {
        return customerFlag;
    }

    public void setCustomerFlag(Short customerFlag) {
        this.customerFlag = customerFlag;
    }

    public Short getCustomerMode() {
        return customerMode;
    }

    public void setCustomerMode(Short customerMode) {
        this.customerMode = customerMode;
    }

    public Short getSubAccountFlag() {
        return subAccountFlag;
    }

    public void setSubAccountFlag(Short subAccountFlag) {
        this.subAccountFlag = subAccountFlag;
    }

    public Boolean getShopOrderFlag() {
        return shopOrderFlag;
    }

    public void setShopOrderFlag(Boolean shopOrderFlag) {
        this.shopOrderFlag = shopOrderFlag;
    }
}
