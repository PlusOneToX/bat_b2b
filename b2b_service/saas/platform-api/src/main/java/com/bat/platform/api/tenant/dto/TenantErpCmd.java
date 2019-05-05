package com.bat.platform.api.tenant.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "ERP配置添加或修改")
public class TenantErpCmd {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @NotNull(message = "P_PLATFORM_TENANT_ID_NULL")
    @ApiModelProperty(value = "平台租户id")
    private Integer tenantId;

    @NotBlank(message = "P_PLATFORM_TENANT_NO_NULL")
    @ApiModelProperty(value = "平台租户编码")
    private String tenantNo;

    @NotNull(message = "P_PLATFORM_ERP_TYPE_NULL")
    @ApiModelProperty(value = "erp类型：1 金蝶")
    private Short erpType;

    @NotBlank(message = "P_PLATFORM_BASE_URL_NULL")
    @ApiModelProperty(value = "erp 基础url")
    private String baseUrl;

    @NotBlank(message = "P_PLATFORM_BASE_EXT_URL_NULL")
    @ApiModelProperty(value = "erp 基础扩展url")
    private String baseExtUrl;

    @NotBlank(message = "P_PLATFORM_USER_NAME_NULL")
    @ApiModelProperty(value = "erp用户名")
    private String userName;

    @NotBlank(message = "P_PLATFORM_PASSWORD_NULL")
    @ApiModelProperty(value = "erp用户登录密码")
    private String password;

    @NotBlank(message = "P_PLATFORM_DB_ID_NULL")
    @ApiModelProperty(value = "erp数据库id")
    private String dbId;

    @NotBlank(message = "P_PLATFORM_LANG_NULL")
    @ApiModelProperty(value = "erp 语言'")
    private String lang;

    @NotBlank(message = "P_PLATFORM_NULL")
    @ApiModelProperty(value = "erp 平台")
    private String platform;

    @NotNull(message = "P_PLATFORM_TOCKEN_VALID_TIME_NULL")
    @ApiModelProperty(value = "token有效时间")
    private Integer tokenValidTime;

    @NotBlank(message = "P_PLATFORM_WLF_ITEM_NO_NULL")
    @ApiModelProperty(value = "erp 物流费用编码")
    private String wlfItemNo;

    @NotBlank(message = "P_PLATFORM_VMI_WAREHOUSE_NULL")
    @ApiModelProperty(value = "erp vmi仓库编码")
    private String vmiWarehouse;

    @NotBlank(message = "P_PLATFORM_PO_INBOUND_TYPE_NULL")
    @ApiModelProperty(value = "erp 采购入库单据类型,多个情况中间用逗号隔开")
    private String poInboundType;

    @NotBlank(message = "P_PLATFORM_SETTLE_DEFAULT_NULL")
    @ApiModelProperty(value = "erp 订单缺省结算方式编码")
    private String settleDefault;

    @NotBlank(message = "P_PLATFORM_SETTLE_CASE_ONLINE_NULL")
    @ApiModelProperty(value = "erp 订单线上结算方式编码")
    private String settleCashOnline;

    @NotBlank(message = "P_PLATFORM_SETTLE_CASE_OFFLINE_NULL")
    @ApiModelProperty(value = "erp 订单线下结算方式编码")
    private String settleCashOffline;

    @NotBlank(message = "P_PLATFORM_SETTLE_MONTH_NULL")
    @ApiModelProperty(value = "erp 月结结算方式编码")
    private String settleMonth;

}
