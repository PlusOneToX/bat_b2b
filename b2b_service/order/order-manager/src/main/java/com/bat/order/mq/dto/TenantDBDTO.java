package com.bat.order.mq.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/7/8 20:44
 */
@Data
public class TenantDBDTO implements Serializable {
    /**
     * 租户编码tenantNo
     */
    private String tenantNo;
    /**
     * 数据库
     */
    private String dbName;
    /**
     * 服务模块：1-商品服务 2-客户服务 3-仓库服务 4-系统服务 5-柔性定制服务 6-营销推广服务 7-订单服务 8-财务服务 9-第三方接口服务 10-mongodb
     */
    private Short modelType;

    private String mongodbHost;

    private String mongodbPort;

    private String mongodbDatabase;

    private String userName;

    private String password;

}
