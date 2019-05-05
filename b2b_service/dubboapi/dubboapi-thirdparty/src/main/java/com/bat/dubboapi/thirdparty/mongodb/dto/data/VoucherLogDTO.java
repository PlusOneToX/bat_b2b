package com.bat.dubboapi.thirdparty.mongodb.dto.data;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 沙漠
 */
@Data
public class VoucherLogDTO implements Serializable {

    /**
     * 收款单id
     */
    private Integer voucherId;
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 操作来源 "platform"
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
