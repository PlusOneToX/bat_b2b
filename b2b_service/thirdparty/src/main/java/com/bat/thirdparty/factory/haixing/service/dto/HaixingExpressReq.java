package com.bat.thirdparty.factory.haixing.service.dto;

import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/30 19:18
 */
@Data
public class HaixingExpressReq {
    /**
     * 订单ID
     */
    private Integer orderId;
    /**
     * 商家单号
     */
    private String sellerOrderNo;
    /**
     * 海星订单号
     */
    private String orderNo;
    /**
     * 物流单号
     */
    private String lcOrderno;
    /**
     * 物流公司编码
     */
    private String lcCode;
    /**
     * 物流公司名称
     */
    private String lcName;
}
