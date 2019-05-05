package com.bat.dubboapi.thirdparty.mongodb.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/8 20:44
 */
@Data
public class OrderLogCmd implements Serializable {
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 操作来源 1系统 2 第三方系统 3 业务员 4 分销商 5 C端客户
     */
    private Short operateSource;
    /**
     * 操作人id
     */
    private Integer operateId;
    /**
     * 操作人名称
     */
    private String operator;
    /**
     * 操作时间
     */
    private Date operateTime;
    /**
     * 操作类型
     */
    private String operateType;
    /**
     * 操作说明
     */
    private String operateDes;
    /**
     * 操作数据
     */
    private String operateData;
}
