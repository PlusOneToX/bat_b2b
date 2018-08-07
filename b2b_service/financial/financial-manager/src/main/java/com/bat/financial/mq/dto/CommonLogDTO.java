package com.bat.financial.mq.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/7/8 20:44
 */
@Data
public class CommonLogDTO implements Serializable {
    /**
     * 业务模块
     */
    private String businessModule;
    /**
     * 业务功能
     */
    private String businessFunction;
    /**
     * 业务id
     */
    private Integer businessId;
    /**
     * 操作来源
     */
    private String operateSource;
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
