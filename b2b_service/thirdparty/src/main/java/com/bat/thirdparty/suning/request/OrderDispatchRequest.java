package com.bat.thirdparty.suning.request;

public class OrderDispatchRequest {

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 作业人员一代码
     */
    private String zyry1Bp;

    /**
     * 作业人员一名称
     */
    private String zyry1BpName;

    /**
     * 作业人员一联系方式
     */
    private String zyry1BpTel;

    /**
     * 最新派工时间
     */
    private String latestAssignmengtTime;

    /**
     * 预约作业时间
     */
    private String srvTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getZyry1Bp() {
        return zyry1Bp;
    }

    public void setZyry1Bp(String zyry1Bp) {
        this.zyry1Bp = zyry1Bp;
    }

    public String getZyry1BpName() {
        return zyry1BpName;
    }

    public void setZyry1BpName(String zyry1BpName) {
        this.zyry1BpName = zyry1BpName;
    }

    public String getZyry1BpTel() {
        return zyry1BpTel;
    }

    public void setZyry1BpTel(String zyry1BpTel) {
        this.zyry1BpTel = zyry1BpTel;
    }

    public String getLatestAssignmengtTime() {
        return latestAssignmengtTime;
    }

    public void setLatestAssignmengtTime(String latestAssignmengtTime) {
        this.latestAssignmengtTime = latestAssignmengtTime;
    }

    public String getSrvTime() {
        return srvTime;
    }

    public void setSrvTime(String srvTime) {
        this.srvTime = srvTime;
    }
}
