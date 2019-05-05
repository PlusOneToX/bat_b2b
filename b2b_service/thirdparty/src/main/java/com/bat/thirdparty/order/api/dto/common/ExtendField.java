package com.bat.thirdparty.order.api.dto.common;

import io.swagger.annotations.ApiModelProperty;

/**
 * 外方下单扩展字段
 */
public class ExtendField {

    /**
     * 刚进去编辑器的URL
     */
    @ApiModelProperty(value = "刚进去编辑器的URL")
    private String urlParam;

    @ApiModelProperty(value = "第三方用户编码、进来编辑器带进来的")
    private String userId;

    @ApiModelProperty(value = "第三方的订单类型、非bat的参数")
    private String orderType;

    @ApiModelProperty(value = "第三方带进来编辑器的参数")
    private String userProductId;

    @ApiModelProperty(value = "第三方进来编辑器带的参数")
    private String orderId;

    @ApiModelProperty(value = "第三方进来编辑器携带的参数")
    private String backUrl;

    public String getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getUserProductId() {
        return userProductId;
    }

    public void setUserProductId(String userProductId) {
        this.userProductId = userProductId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }
}
