package com.bat.thirdparty.mongodb.dao.dataobject;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收款单日志
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document // 表示映射为mongoDB数据库内一个集合
public class VoucherLogDO implements Serializable {
    @Id
    private String _id;
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
