package com.bat.thirdparty.mongodb.dao.dataobject;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 退款单日志
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document // 表示映射为mongoDB数据库内一个集合
public class RefundLogDO implements Serializable {
    @Id
    private String _id;
    /**
     * 退款单id
     */
    private Integer refundId;
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
