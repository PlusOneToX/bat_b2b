package com.bat.dubboapi.order.order.dto.factory.haixing;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 海星提交订单对象 订单同步到海星（脚本类-放同一个包方便管理）
 * 
 * @author Administrator
 */
@Data
public class HaiXingOrderQueCmd implements Serializable {
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
     * 状态ID
     */
    private Integer orderStateId;
    /**
     * 创建时间(大于)
     */
    private Date startTime;
    /**
     * 创建时间(小于)
     */
    private Date endTime;
    /**
     * 联系人（模糊搜索）
     */
    private String contacts;
    /**
     * 商家备注（模糊搜索）
     */
    private String memo;
    /**
     * 商品名称（模糊搜索）
     */
    private String goodsName;
    /**
     * 页数（从1开始）
     */
    private Integer page;
    /**
     * 每页行数（最大50）
     */
    private Integer rows;
}
