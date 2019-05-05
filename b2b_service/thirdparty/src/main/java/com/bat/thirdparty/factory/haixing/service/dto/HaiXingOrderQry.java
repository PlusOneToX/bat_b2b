package com.bat.thirdparty.factory.haixing.service.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 海星提交订单对象 订单同步到海星（脚本类-放同一个包方便管理）
 * 
 * @author Administrator
 */
@Data
public class HaiXingOrderQry implements Serializable {
    /**
     * 是 商家单号（唯一）
     */
    private String sellerOrderNo;

    private String orderNo;

    /**
     * 订单ID
     */
    private Integer orderId;

    private Date startTime;

    private Date endTime;

}
