package com.bat.dubboapi.platform.tenant.dto.data;

import java.io.Serializable;
import java.util.Date;

public class PlatformTenantDBRpcDTO implements Serializable {
    private Integer id;

    private Integer tenantId;

    private String tenantNo;
    /**
     * 服务模块：1-商品服务 2-客户服务 3-仓库服务 4-系统服务 5-柔性定制服务 6-营销推广服务 7-订单服务 8-财务服务 9-第三方接口服务 10-mongodb 11-redis
     */
    private Short modelType;

    private Short dbType;
    private Short sourceType;
    private Short tableFlag;

    private String dbName;
    private String dbBaseUrl;
    private String dbUrl;

    private String host;

    private String port;

    private String nosqlDatabase;

    private String userName;

    private String password;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantNo() {
        return tenantNo;
    }

    public void setTenantNo(String tenantNo) {
        this.tenantNo = tenantNo;
    }

    public Short getModelType() {
        return modelType;
    }

    public void setModelType(Short modelType) {
        this.modelType = modelType;
    }

    public Short getDbType() {
        return dbType;
    }

    public void setDbType(Short dbType) {
        this.dbType = dbType;
    }

    public Short getSourceType() {
        return sourceType;
    }

    public void setSourceType(Short sourceType) {
        this.sourceType = sourceType;
    }

    public Short getTableFlag() {
        return tableFlag;
    }

    public void setTableFlag(Short tableFlag) {
        this.tableFlag = tableFlag;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbBaseUrl() {
        return dbBaseUrl;
    }

    public void setDbBaseUrl(String dbBaseUrl) {
        this.dbBaseUrl = dbBaseUrl;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getNosqlDatabase() {
        return nosqlDatabase;
    }

    public void setNosqlDatabase(String nosqlDatabase) {
        this.nosqlDatabase = nosqlDatabase;
    }
}