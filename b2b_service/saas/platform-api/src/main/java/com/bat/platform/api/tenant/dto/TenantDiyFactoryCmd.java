package com.bat.platform.api.tenant.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "定制工厂配置添加或修改")
public class TenantDiyFactoryCmd {

    private Integer id;

    @NotNull(message = "P_PLATFORM_TENANT_ID_NULL")
    @ApiModelProperty(value = "平台租户id")
    private Integer tenantId;

    @NotBlank(message = "P_PLATFORM_TENANT_NO_NULL")
    @ApiModelProperty(value = "平台租户编码")
    private String tenantNo;

    @NotBlank(message = "P_PLATFORM_FACTORY_NO_NULL")
    @ApiModelProperty(value = "MK 麦克 LY 烙印")
    private String factoryNo;

    @NotBlank(message = "P_PLATFORM_FACTORY_NAME_NULL")
    @ApiModelProperty(value = "工厂名称")
    private String factoryName;

    @NotBlank(message = "P_PLATFORM_APPID_NULL")
    @ApiModelProperty(value = "工厂的appid")
    private String appId;

    @NotBlank(message = "P_PLATFORM_APP_KEY_NULL")
    @ApiModelProperty(value = "工厂的appkey")
    private String appKey;

    @NotBlank(message = "P_PLATFORM_APP_SECRET_NULL")
    @ApiModelProperty(value = "工厂的appSecret")
    private String appSecret;

    @NotBlank(message = "P_PLATFORM_ORDER_ADD_URL_NULL")
    @ApiModelProperty(value = "工厂添加订单url")
    private String orderAddUrl;

    @NotBlank(message = "P_PLATFORM_ORDER_CANCEL_URL_NULL")
    @ApiModelProperty(value = "工厂取消订单url")
    private String orderCancelUrl;

    @NotBlank(message = "P_PLATFORM_SUPPLIER_NO_NULL")
    @ApiModelProperty(value = "工厂供应商编码")
    private String supplierNo;

    @NotBlank(message = "P_PLATFORM_WAREHOUSE_NO_NULL")
    @ApiModelProperty(value = "工厂仓库编码")
    private String warehouseNo;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "工厂其他配置内容")
    private String configOther;

}
