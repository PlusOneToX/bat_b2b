package com.bat.thirdparty.mongodb.dao.dataobject;

import java.io.Serializable;

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
public class ErpFailLogDO implements Serializable {
    @Id
    private String _id;
    /**
     * 数据同步类型 1 erp订单变更 2 erp订单审核
     */
    private Short type;
    /**
     * 同步失败说明
     */
    private String failMsg;
    /**
     * 同步数据
     */
    private String data;
}
