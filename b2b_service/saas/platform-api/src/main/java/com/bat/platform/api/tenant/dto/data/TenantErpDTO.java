package com.bat.platform.api.tenant.dto.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class TenantErpDTO {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "平台租户id")
    private Integer tenantId;

    @ApiModelProperty(value = "平台租户编码")
    private String tenantNo;

    @ApiModelProperty(value = "erp类型：1 金蝶")
    private Short erpType;

    @ApiModelProperty(value = "erp 基础url")
    private String baseUrl;

    @ApiModelProperty(value = "erp 基础扩展url")
    private String baseExtUrl;

    @ApiModelProperty(value = "erp用户名")
    private String userName;

    @ApiModelProperty(value = "erp用户登录密码")
    private String password;

    @ApiModelProperty(value = "erp数据库id")
    private String dbId;

    @ApiModelProperty(value = "erp 语言'")
    private String lang;

    @ApiModelProperty(value = "erp 平台")
    private String platform;

    @ApiModelProperty(value = "token有效时间")
    private Integer tokenValidTime;

    @ApiModelProperty(value = "erp 物流费用编码")
    private String wlfItemNo;

    @ApiModelProperty(value = "erp vmi仓库编码")
    private String vmiWarehouse;

    @ApiModelProperty(value = "erp 采购入库单据类型,多个情况中间用逗号隔开")
    private String poInboundType;

    @ApiModelProperty(value = "erp 订单缺省结算方式编码")
    private String settleDefault;

    @ApiModelProperty(value = "erp 订单线上结算方式编码")
    private String settleCashOnline;

    @ApiModelProperty(value = "erp 订单线下结算方式编码")
    private String settleCashOffline;

    @ApiModelProperty(value = "erp 月结结算方式编码")
    private String settleMonth;


    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
