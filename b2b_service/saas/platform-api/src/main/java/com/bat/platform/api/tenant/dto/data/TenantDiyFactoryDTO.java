package com.bat.platform.api.tenant.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TenantDiyFactoryDTO {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "平台租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "平台租户编码")
    private String tenantNo;

    @ApiModelProperty(value = "MK 麦克 LY 烙印")
    private String factoryNo;

    @ApiModelProperty(value = "工厂名称")
    private String factoryName;

    @ApiModelProperty(value = "工厂的appid")
    private String appId;

    @ApiModelProperty(value = "工厂的appkey")
    private String appKey;

    @ApiModelProperty(value = "工厂的appSecret")
    private String appSecret;

    @ApiModelProperty(value = "工厂添加订单url")
    private String orderAddUrl;

    @ApiModelProperty(value = "工厂取消订单url")
    private String orderCancelUrl;

    @ApiModelProperty(value = "工厂供应商编码")
    private String supplierNo;

    @ApiModelProperty(value = "工厂仓库编码")
    private String warehouseNo;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "工厂其他配置内容")
    private String configOther;

}
