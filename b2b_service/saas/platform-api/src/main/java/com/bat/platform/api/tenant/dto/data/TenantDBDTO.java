package com.bat.platform.api.tenant.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "平台租户数据库信息")
public class TenantDBDTO {
    @ApiModelProperty(value = "ID")
    private Integer id;
    @ApiModelProperty(value = "平台租户id", example = "100")
    private Integer tenantId;
    @ApiModelProperty(value = "平台租户编码", example = "bat")
    private String tenantNo;
    @ApiModelProperty(value = "服务模块：1-商品服务 2-客户服务 3-仓库服务 4-系统服务 5-柔性定制服务 6-营销推广服务 7-订单服务 8-财务服务 9-第三方接口服务 10-mongodb",
        example = "1")
    private Short modelType;
    @ApiModelProperty(value = "数据库类型： 1-独立库 2-共享库", example = "1")
    private Short dbType;
    @ApiModelProperty(value = "数据库源来源：1-自动生成 2-手工填写", example = "1")
    private Short sourceType;
    @ApiModelProperty(value = "数据表状态：1-已创建 2-未创建(数据库来源为手工填写默认已创建)", example = "1")
    private Short tableFlag;
    @ApiModelProperty(value = "数据库连接 base url",
        example = "jdbc:mysql://rm-bp1d09bcp557cm0hkzo.mysql.rds.aliyuncs.com:3306/")
    private String dbBaseUrl;
    @ApiModelProperty(value = "数据库源名称", example = "bat")
    private String dbName;
    @ApiModelProperty(value = "数据库连接url", example = "bat")
    private String dbUrl;
    @ApiModelProperty(value = "主机ip或域名", example = "bat")
    private String host;
    @ApiModelProperty(value = "端口", example = "bat")
    private String port;
    @ApiModelProperty(value = "数据库", example = "bat")
    private String nosqlDatabase;
    @ApiModelProperty(value = "数据库用户名，最长10个字符", example = "bat")
    private String userName;
    @ApiModelProperty(value = "数据库连接用户登录密码", example = "bat")
    private String password;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}