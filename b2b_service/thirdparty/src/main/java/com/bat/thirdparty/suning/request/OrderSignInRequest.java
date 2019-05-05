package com.bat.thirdparty.suning.request;

import lombok.Data;

/**
 * 服务商反馈现场签到传参
 */
@Data
public class OrderSignInRequest {
    /**
     * 服务订单
     */
    private String orderId;
    /**
     * 作业人员
     */
    private String workerBp;
    /**
     * 上门时间
     */
    private String latestDoorTime;
    /**
     * 上门维度
     */
    private String smWd;
    /**
     * 上门经度
     */
    private String smJd;
    /**
     * 上门地址
     */
    private String smAddress;
    /**
     * 上门真实标识
     */
    private String smZsbs;




}
