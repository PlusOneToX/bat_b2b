package com.bat.thirdparty.factory.common.service.dto;

import lombok.Data;

@Data
public class OrderDeliveryCommonResp {
    /**
     * 是否手动发货
     */
    private Boolean manualFlag = false;

    /**
     * 快递单号
     */
    private String expressNo;

    /**
     * 快递公司编码
     */
    private String expressCode;

    /**
     * 快递公司名称
     */
    private String expressName;

    /**
     * 发货时间(毫秒)
     */
    private Long expressTime;

    /**
     * 订单号（先查工厂单号 再查订单id）
     */
    private String orderNo;
}
