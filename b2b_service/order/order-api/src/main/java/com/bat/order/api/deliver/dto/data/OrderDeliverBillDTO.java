package com.bat.order.api.deliver.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/7 16:56
 */
@Data
public class OrderDeliverBillDTO {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单id")
    private Integer orderId;
    @ApiModelProperty("配送id")
    private Integer distributionId;
    @ApiModelProperty("配送方式编码")
    private String distributionCode;
    @ApiModelProperty("配送方式名称")
    private String distributionName;
    @ApiModelProperty("发货状态 1待确认,2.已确认,3.已取消")
    private Short deliverStatus;
    @ApiModelProperty("物流编号")
    private String logisticsNo;
    @ApiModelProperty("物流状态：2-在途中,3-签收,4-问题件")
    private String logisticsStatus;
    @ApiModelProperty("erp出库单号")
    private String deliverErpNo;
    @ApiModelProperty("erp采购单号")
    private String deliverStkNo;
    @ApiModelProperty("是否需要推送第三方,1 为是需要推送，0为不需要推送")
    private Short push;
    @ApiModelProperty("同步给第三方成功与否: 1为同步成功，0为同步失败")
    private Short pushStatus;
    @ApiModelProperty("发货时间")
    private Date deliverTime;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
