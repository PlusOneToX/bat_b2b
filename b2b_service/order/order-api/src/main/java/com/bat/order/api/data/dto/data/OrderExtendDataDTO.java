package com.bat.order.api.data.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: 订单扩展信息
 * @date: 2018/6/24 14:43
 */
@Data
public class OrderExtendDataDTO {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单id")
    private Integer orderId;
    @ApiModelProperty("第三方客户订单编号")
    private String orderThirdpartyNo;
    @ApiModelProperty("erp订单编号")
    private String orderErpNo;
    @ApiModelProperty("erp订单类型")
    private String orderErpType;
    @ApiModelProperty("工厂订单编号")
    private String orderFactoryNo;
    @ApiModelProperty("工厂")
    private String factoryCode;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("自动下推出库 1.是 0.否")
    private Short autoDelivery;
}
