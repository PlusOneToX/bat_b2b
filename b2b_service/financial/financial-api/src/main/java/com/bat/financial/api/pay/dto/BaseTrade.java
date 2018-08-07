package com.bat.financial.api.pay.dto;

import javax.validation.constraints.Pattern;

import com.bat.financial.api.pay.constant.PayChannel;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 20:18
 */
public class BaseTrade {

    @ApiModelProperty(value = "C端客户标志 0 不是C端客户(默认B2B) 1是C端客户", example = "0")
    private Short customerFlag = 0;

    @Pattern(
        regexp = "(alipay_common|alipay_huabei|alipay_face_to_face|alipay_page|alipay_wap|alipay_app|wxpay_jsapi|wxpay_app|wxpay_h5|wxpay_native|wxpay_mini_program|wxpay_combine|balance|kuaiqian_merchant|kuaiqian_quick)",
        message = "P_TRADE_PAY_METHOD_NULL")
    @ApiModelProperty(
        value = "交易方法 (alipay_common|alipay_huabei|alipay_face_to_face|alipay_page|alipay_wap|alipay_app|wxpay_jsapi|wxpay_app|wxpay_h5|wxpay_native|wxpay_mini_program|wxpay_combine|balance|kuaiqian_merchant|kuaiqian_quick)",
        required = true, example = "wxpay_native")
    private String payMethod;

    @ApiModelProperty(value = "appId(授权时的appId)")
    private String appId;

    /**
     * @ApiModelProperty(value = "业务类型 1订单收款2在线充值收款", example = "2") 订单号有值默认为订单，不然为充值
     */
    private Short businessType;

    /**
     * @Pattern(regexp = "(wxpay|alipay|kuaiqian|balance)", message = "P_TRADE_PAY_TYPE_NULL")
     * @ApiModelProperty(value = "交易渠道 wxpay 微信 alipay支付宝 balance余额支付 kuaiqian快钱", required = true, example = "wxpay")
     * 
     *                         由订单服务确定
     */
    private String payChannel;

    /**
     * @NotNull(message = "P_ACCOUNT_ALIPAY_ORGANIZATION_ID_NULL")
     * @ApiModelProperty(value = "销售组织id(不传)", required = false, example = "1")
     * 
     *                         由订单服务确定
     */
    private Integer organizationId;

    /**
     * @NotBlank(message = "P_ACCOUNT_CURRENCY_CODE_NULL")
     * @ApiModelProperty(value = "币别编码 默认都是CNY(不传)", example = "CNY")
     * 
     *                         由订单服务确定
     */
    private String currencyCode = "CNY";

    /**
     * @ApiModelProperty(value = "1 平台方收款(比如：bat收款，bat收款), 2 自己收款(分销商自己收款)(不传)", example = "")
     * 
     * 
     *                         由订单服务确定
     */
    private Short tradeMode;

    /**
     * @ApiModelProperty(value = "收款人Id 在分销商收款下适用", required = true, example = "10086")
     * 
     *                         由订单服务确定
     */
    private Integer payeeId;

    /**
     * 1 微信公众号 2 微信小程序
     */
    private Short appType;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Short getCustomerFlag() {
        return customerFlag;
    }

    public void setCustomerFlag(Short customerFlag) {
        this.customerFlag = customerFlag;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Short getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Short businessType) {
        this.businessType = businessType;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Short getTradeMode() {
        return tradeMode;
    }

    public void setTradeMode(Short tradeMode) {
        this.tradeMode = tradeMode;
    }

    public Integer getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Integer payeeId) {
        this.payeeId = payeeId;
    }

    public Short getAppType() {
        return this.payMethod.toUpperCase().equals(PayChannel.WXPAY_MINI_PROGRAM.name()) ? (short)2 : (short)1;
    }

    public void setAppType(Short appType) {
        this.appType = appType;
    }
}
