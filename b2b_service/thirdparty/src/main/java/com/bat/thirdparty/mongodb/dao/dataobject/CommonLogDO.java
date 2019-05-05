package com.bat.thirdparty.mongodb.dao.dataobject;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/8 20:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document // 表示映射为mongoDB数据库内一个集合
public class CommonLogDO implements Serializable {
    @Id
    private String _id;
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
     * 操作类型 如"分销商下单"
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
