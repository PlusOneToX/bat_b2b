package com.bat.platform.api.tenant.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "新增修改平台租户数据库信息")
public class TenantDBCmd {
    @ApiModelProperty(value = "ID(修改时必填)", required = false)
    private Integer id;
    @NotNull(message = "P_PLATFORM_TENANT_ID_NULL")
    @ApiModelProperty(value = "平台租户id", required = true, example = "100")
    private Integer tenantId;
    @NotBlank(message = "P_PLATFORM_TENANT_NO_NULL")
    @ApiModelProperty(value = "平台租户编码(所有的字母将转换为小写)", required = true, example = "100")
    private String tenantNo;
    @NotNull(message = "P_PLATFORM_MODEL_TYPE_NULL")
    @ApiModelProperty(value = "服务模块：1-商品服务 2-客户服务 3-仓库服务 4-系统服务 5-柔性定制服务 6-营销推广服务 7-订单服务 8-财务服务 9-第三方接口服务 10-mongodb",
        required = true, example = "1")
    private Short modelType;
    @NotNull(message = "P_PLATFORM_DB_TYPE_NULL")
    @ApiModelProperty(value = "数据库类型(服务模块为非10时必填)： 1-独立库 2-共享库", required = true, example = "1")
    private Short dbType;
    @NotNull(message = "P_PLATFORM_SOURCE_TYPE_NULL")
    @ApiModelProperty(value = "数据库源来源(新增平台租户数据库必填)：1-自动生成 2-手工填写", required = false, example = "1")
    private Short sourceType;
    @ApiModelProperty(value = "数据表状态：1-已创建 2-未创建(数据库来源为手工填写默认已创建)", required = false, example = "1")
    private Short tableFlag;
    @ApiModelProperty(value = "数据库连接 base url", required = false, example = "jdbc:mysql://127.0.0.1:3306/")
    private String dbBaseUrl;
    @ApiModelProperty(value = "数据库源名称(所有的字母将转换为小写)", required = false, example = "bat")
    private String dbName;
    @ApiModelProperty(value = "数据库连接url", required = false, example = "bat")
    private String dbUrl;
    @ApiModelProperty(value = "nosql ip或域名(服务模块为10情况必填)", required = false, example = "bat")
    private String host;
    @ApiModelProperty(value = "nosql 端口(服务模块为10情况必填)", required = false, example = "bat")
    private String port;
    @ApiModelProperty(value = "nosql 数据库(服务模块为10情况必填)", required = false, example = "bat")
    private String nosqlDatabase;
    @NotBlank(message = "P_PLATFORM_USER_NAME_NULL")
    @ApiModelProperty(value = "数据库用户名，最长10个字符", required = true, example = "bat")
    private String userName;
    @NotBlank(message = "P_PLATFORM_PASSWORD_NULL")
    @ApiModelProperty(value = "数据库连接用户登录密码(HD5加密)", required = true, example = "bat")
    private String password;
}