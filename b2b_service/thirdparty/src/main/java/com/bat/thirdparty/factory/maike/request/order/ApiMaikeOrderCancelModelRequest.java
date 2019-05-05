package com.bat.thirdparty.factory.maike.request.order;

import javax.validation.constraints.NotBlank;

public class ApiMaikeOrderCancelModelRequest {
    /**
     * bat订单号
     */
    @NotBlank(message = "bat订单号不能为空")
    private String orderNo;

    /**
     * 麦克订单号
     */
    @NotBlank(message = "麦克订单号不能为空")
    private String maikeOrderNo;

    /**
     * 麦客用户名
     */
    @NotBlank(message = "麦客用户名不能为空")
    private String maikeUsername;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空")
    private String remarks;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMaikeOrderNo() {
        return maikeOrderNo;
    }

    public void setMaikeOrderNo(String maikeOrderNo) {
        this.maikeOrderNo = maikeOrderNo;
    }

    public String getMaikeUsername() {
        return maikeUsername;
    }

    public void setMaikeUsername(String maikeUsername) {
        this.maikeUsername = maikeUsername;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "ApiMaikeOrderCancelModelRequest{" + "orderNo='" + orderNo + '\'' + ", maikeOrderNo='" + maikeOrderNo
            + '\'' + ", maikeUsername='" + maikeUsername + '\'' + ", remarks='" + remarks + '\'' + '}';
    }
}
