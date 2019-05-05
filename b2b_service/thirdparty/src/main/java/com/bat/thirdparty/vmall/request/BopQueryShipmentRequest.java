package com.bat.thirdparty.vmall.request;

import lombok.Data;

import java.util.List;

@Data
public class BopQueryShipmentRequest {
    /**
     * 订单编码，支持多个，多个以英文逗号隔开
     */
    private String orderNo;

    /**
     * 发货单状态(10-未发货,11-取消,14-出库失败 ，15-已出库,20-已发货,30-已签收,31-拒签,40-丢失，81-已安排发货)
     */
    private Short status;
}
