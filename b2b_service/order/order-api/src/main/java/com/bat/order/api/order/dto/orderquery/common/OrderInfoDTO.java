package com.bat.order.api.order.dto.orderquery.common;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: 订单基本信息
 * @date: 2018/6/22 12:42
 */
@Data
public class OrderInfoDTO {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单编号")
    private String orderNo;
    @ApiModelProperty("发货状态 1.未发货2.出库中 3.部分发货 4.已发货 ")
    private Short deliverStatus;
    @ApiModelProperty("发货时间(发货状态为部分发货或已发货时有值)")
    private Date deliverTime;
    @ApiModelProperty("订单类型id")
    private Integer orderTypeId;
    @ApiModelProperty("扩展类型：订单类型名称")
    private Integer orderTypeName;
    @ApiModelProperty("库存类型：1在库 2在途 3预售(mto)  4 在途+在库 5、委外")
    private Short stockType;
    @ApiModelProperty("业务员id")
    private Integer salesId;
    @ApiModelProperty("业务员姓名")
    private String salesName;
    @ApiModelProperty("商务员id")
    private Integer coordinatorId;
    @ApiModelProperty("商务员姓名")
    private String coordinatorName;
    @ApiModelProperty("是否开具发票 0.否，1.是")
    private Short invoiceFlag;
    @ApiModelProperty("订单来源：分销平台编码")
    private String orderSource;
    @ApiModelProperty("扩展字段：订单来源：分销平台编码名称")
    private String orderSourceName;
    @ApiModelProperty("业务语言：zh 中文 en 英文 ")
    private String language;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
